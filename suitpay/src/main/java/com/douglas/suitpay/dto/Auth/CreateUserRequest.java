package com.douglas.suitpay.dto.Auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {
    @NotBlank @Size(min = 3, max = 150)
    private String username;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    /** Ex.: ["ROLE_USER", "ROLE_CREDIT_LIMIT_ADMIN"] */
    private Set<String> roles;
}
