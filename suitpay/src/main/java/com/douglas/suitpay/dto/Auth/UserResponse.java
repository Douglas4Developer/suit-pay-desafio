package com.douglas.suitpay.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private boolean enabled;
    private Set<String> roles;
}
