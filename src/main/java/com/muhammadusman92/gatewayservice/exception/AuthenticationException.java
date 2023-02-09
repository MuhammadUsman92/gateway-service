package com.muhammadusman92.gatewayservice.exception;

import lombok.Data;

@Data
public class AuthenticationException {
    private String errorCode;
    private String error;
    private String errorDetails;
    public AuthenticationException(String errorCode, String error, String errorDetails) {
        this.errorCode = errorCode;
        this.error = error;
        this.errorDetails = errorDetails;
    }
}
