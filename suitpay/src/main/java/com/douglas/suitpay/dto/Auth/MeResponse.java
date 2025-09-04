package com.douglas.suitpay.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeResponse {
    private String username;
    private List<String> roles;
}