package com.projects.vehicle_renting.config;

import com.projects.vehicle_renting.model.User;
import com.projects.vehicle_renting.model.enums.Role;
import com.projects.vehicle_renting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@vehicle-renting.com}")
    private String adminEmail;

    @Value("${admin.password:Admin@1234}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .name("System Administrator")
                .phone("00000000000")
                .role(Role.ADMIN)
                .walletBalance(BigDecimal.ZERO)
                .build();

        userRepository.save(admin);
        log.info("Seeded default admin account: {}", adminEmail);
    }
}