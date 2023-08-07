package com.example.openoff.domain.auth.application.service;

import com.example.openoff.common.dto.ResponseDto;
import com.example.openoff.domain.auth.application.dto.request.SocialSignupRequestDto;
import com.example.openoff.domain.auth.application.dto.request.apple.AppleOIDCRequestDto;
import com.example.openoff.domain.auth.application.dto.request.google.GoogleOAuthCodeRequestDto;
import com.example.openoff.domain.auth.application.dto.request.kakao.KakaoOIDCRequestDto;
import com.example.openoff.domain.auth.application.dto.request.normal.NormalSignInRequestDto;
import com.example.openoff.domain.auth.application.dto.request.normal.ResetPasswordRequestDto;
import com.example.openoff.domain.auth.application.dto.response.apple.AppleUserInfoResponseDto;
import com.example.openoff.domain.auth.application.dto.response.google.GoogleUserInfoResponseDto;
import com.example.openoff.domain.auth.application.dto.response.kakao.KakaoUserInfoResponseDto;
import com.example.openoff.domain.auth.application.dto.response.normal.CheckEmailResponseDto;
import com.example.openoff.domain.auth.application.dto.response.normal.SearchIdResponseDto;
import com.example.openoff.domain.auth.application.dto.response.token.TokenResponseDto;

public interface AuthService {
    // Token issue
    ResponseDto<TokenResponseDto> tokenRefresh(TokenResponseDto tokenResponseDto);
    // Sign In
    ResponseDto<TokenResponseDto> initSocialSignIn(SocialSignupRequestDto socialSignupRequestDto, String provider);

    // GOOGLE
    GoogleUserInfoResponseDto getGoogleUserInfoByAuthCode(GoogleOAuthCodeRequestDto googleOAuthCodeRequestDto);

    // KAKAO
    KakaoUserInfoResponseDto getKakaoUserInfoByIdToken(KakaoOIDCRequestDto kakaoOIDCRequestDto);

    // APPLE
    AppleUserInfoResponseDto getAppleUserInfoByIdToken(AppleOIDCRequestDto appleOIDCRequestDto);

    // NORMAL
    ResponseDto<CheckEmailResponseDto> checkExistEmail(String email);
    ResponseDto<SearchIdResponseDto> searchIdByPhoneNum(String phoneNum);
    ResponseDto<Void> resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
    ResponseDto<TokenResponseDto> initNormalSignUp(NormalSignInRequestDto normalSignupRequestDto);
    ResponseDto<TokenResponseDto> normalLogin(NormalSignInRequestDto normalSignupRequestDto);
}
