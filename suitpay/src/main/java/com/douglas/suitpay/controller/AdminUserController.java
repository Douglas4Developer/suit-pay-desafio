package com.douglas.suitpay.controller;

import com.douglas.suitpay.dto.Auth.CreateUserRequest;
import com.douglas.suitpay.dto.Auth.UserResponse;
import com.douglas.suitpay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /** Cria usuário com roles arbitrárias (apenas admins) */
    @PostMapping
    @PreAuthorize("hasRole('CREDIT_LIMIT_ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        return ResponseEntity.status(201).body(userService.createUser(req));
    }
}
