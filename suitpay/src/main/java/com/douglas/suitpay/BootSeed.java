package com.douglas.suitpay;

import com.douglas.suitpay.domain.Role;
import com.douglas.suitpay.domain.User;
import com.douglas.suitpay.repository.RoleRepository;
import com.douglas.suitpay.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Profile("!test")
public class BootSeed {

    @Bean
    CommandLineRunner seedData(RoleRepository roleRepo,
                               UserRepository userRepo,
                               PasswordEncoder encoder) {
        return args -> {
            // cria roles se não existirem
            Role roleUser = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepo.save(Role.builder().name("ROLE_USER").build()));

            Role roleAdmin = roleRepo.findByName("ROLE_CREDIT_LIMIT_ADMIN")
                    .orElseGet(() -> roleRepo.save(Role.builder().name("ROLE_CREDIT_LIMIT_ADMIN").build()));

            // cria admin inicial
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(encoder.encode("123456"))
                        .enabled(true)
                        .roles(Set.of(roleAdmin, roleUser))  // já com as duas roles
                        .build();
                userRepo.save(admin);
                System.out.println("Usuário admin criado: admin / 123456");
            }
        };
    }
}
