package com.example.openoff.common.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.openoff.common.exception.BusinessException;
import com.example.openoff.common.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.img_bucket}")
    private String bucket;

    public List<String> uploadImgList(List<MultipartFile> imgList) {

        if(Objects.isNull(imgList)) return null;
        if(imgList.isEmpty()) return null;
        List<String> uploadUrl = new ArrayList<>();
        for (MultipartFile img : imgList) {
            uploadUrl.add(uploadImg(img));
        }
        return uploadUrl;
    }


    //단일 이미지 업로드
    public String uploadImg(MultipartFile file) {
        if(Objects.isNull(file)) return null;
        if(file.isEmpty()) return null;

        String originFileName = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);
        String fileName = createFileName(originFileName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw BusinessException.of(Error.FILE_UPLOAD_ERROR);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public List<String> uploadImgs(List<MultipartFile> files) {
        if (Objects.isNull(files) || files.isEmpty()) return Collections.emptyList();

        List<String> uploadedImgUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originFileName = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);
            String fileName = createFileName(originFileName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw BusinessException.of(Error.FILE_UPLOAD_ERROR);
            }

            String uploadedUrl = amazonS3.getUrl(bucket, fileName).toString();
            uploadedImgUrls.add(uploadedUrl);
        }

        return uploadedImgUrls;
    }


    //파일명 난수화
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //파일 확장자 체크
    private String getFileExtension(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        if (!ext.equals(".jpg") && !ext.equals(".png") && !ext.equals(".jpeg") && !ext.equals(".svg+xml") && !ext.equals(".svg")) {
            throw BusinessException.of(Error.FILE_EXTENTION_ERROR);
        }
        return ext;
    }

    public String uploadQRImage(ByteArrayOutputStream out, ByteArrayInputStream inputStream, String ticketIndex) {
        // S3에 업로드할 Object 메타데이터 설정
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(out.size());
        meta.setContentType("image/png");

        // S3에 업로드
        try{
            PutObjectRequest request = new PutObjectRequest(bucket, ticketIndex, inputStream, meta)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);
        } catch (Exception e) {
            throw BusinessException.of(Error.FILE_UPLOAD_ERROR);
        }
        return amazonS3.getUrl(bucket, ticketIndex).toString();
    }
}
