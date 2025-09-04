package com.douglas.suitpay.service;

import com.douglas.suitpay.dto.Auth.CreateUserRequest;
import com.douglas.suitpay.dto.Auth.SignupRequest;
import com.douglas.suitpay.dto.Auth.UserResponse;

public interface UserService {
    UserResponse signup(SignupRequest req);                 // cria ROLE_USER
    UserResponse createUser(CreateUserRequest req);         // cria com roles arbitrárias (apenas admin)
}
