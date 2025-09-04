package com.douglas.suitpay.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String tokenType;       // "Bearer"
    private String accessToken;
    private String refreshToken;
    private long   expiresIn;       // segundos (access)
    private long   refreshExpiresIn;// segundos (refresh)
}
