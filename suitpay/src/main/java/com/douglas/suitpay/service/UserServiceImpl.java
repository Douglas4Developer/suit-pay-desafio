package com.douglas.suitpay.service;// com/douglas/suitpay/service/impl/UserServiceImpl.java


import com.douglas.suitpay.domain.Role;
import com.douglas.suitpay.domain.User;
import com.douglas.suitpay.dto.Auth.CreateUserRequest;
import com.douglas.suitpay.dto.Auth.SignupRequest;
import com.douglas.suitpay.dto.Auth.UserResponse;
import com.douglas.suitpay.exception.BusinessException;
import com.douglas.suitpay.repository.RoleRepository;
import com.douglas.suitpay.repository.UserRepository;
import com.douglas.suitpay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public UserResponse signup(SignupRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new BusinessException("Usuário já existe.");
        }
        Role roleUser = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new BusinessException("ROLE_USER não encontrada. Crie a role primeiro."));

        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .enabled(true)
                .build();
        u.getRoles().add(roleUser);

        userRepo.save(u);

        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.isEnabled(),
                u.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new BusinessException("Usuário já existe.");
        }
        if (req.getRoles() == null || req.getRoles().isEmpty()) {
            throw new BusinessException("Informe pelo menos uma role.");
        }

        var roles = req.getRoles().stream()
                .map(name -> roleRepo.findByName(name)
                        .orElseThrow(() -> new BusinessException("Role inexistente: " + name)))
                .collect(Collectors.toSet());

        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .enabled(true)
                .build();
        u.setRoles(roles);

        userRepo.save(u);

        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.isEnabled(),
                u.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}