package com.muhammadusman92.gatewayservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ConnValidationResponse {
    private String status;
    private boolean isAuthenticated;
    private String methodType;
    private String userName;
    private String userEmail;
    private String token;
    private List<String> authorities;
}
