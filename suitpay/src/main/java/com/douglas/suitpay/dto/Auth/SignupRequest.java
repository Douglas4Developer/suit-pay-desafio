package com.douglas.suitpay.dto.Auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 150)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}