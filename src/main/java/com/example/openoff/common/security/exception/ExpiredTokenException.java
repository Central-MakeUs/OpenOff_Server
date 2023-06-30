package com.example.openoff.common.security.exception;

import com.example.openoff.common.exception.Error;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends JwtException{
    public ExpiredTokenException(Error error, HttpStatus httpStatus) {
        super(error, httpStatus);
    }
}
