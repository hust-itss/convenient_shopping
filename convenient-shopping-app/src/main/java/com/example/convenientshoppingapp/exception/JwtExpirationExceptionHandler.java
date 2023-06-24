package com.example.convenientshoppingapp.exception;

public class JwtExpirationExceptionHandler extends RuntimeException{
    public JwtExpirationExceptionHandler(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
