package com.douglas.suitpay.controller;

import com.douglas.suitpay.dto.Auth.*;
import com.douglas.suitpay.repository.RoleRepository;
import com.douglas.suitpay.repository.UserRepository;
import com.douglas.suitpay.security.JwtService;
import com.douglas.suitpay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        var user = userDetailsService.loadUserByUsername(auth.getName());

        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(
                new TokenResponse("Bearer", access, refresh,
                        jwtService.accessTtlSeconds(), jwtService.refreshTtlSeconds())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        if (!jwtService.isValid(req.getRefreshToken(), "refresh")) {
            throw new BadCredentialsException("Refresh token inválido ou expirado.");
        }
        String username = jwtService.username(req.getRefreshToken());
        var user = userDetailsService.loadUserByUsername(username);

        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user); // rotação simples

        return ResponseEntity.ok(
                new TokenResponse("Bearer", access, refresh,
                        jwtService.accessTtlSeconds(), jwtService.refreshTtlSeconds())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication auth) {
        var roles = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MeResponse(auth.getName(), roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(201).body(userService.signup(req));  // <-- usa o field
    }
}