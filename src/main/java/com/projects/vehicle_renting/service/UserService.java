package com.projects.vehicle_renting.service;

import com.projects.vehicle_renting.dto.RegisterRequest;
import com.projects.vehicle_renting.dto.UserResponse;
import com.projects.vehicle_renting.exception.ConflictException;
import com.projects.vehicle_renting.mapper.UserMapper;
import com.projects.vehicle_renting.model.User;
import com.projects.vehicle_renting.model.enums.Role;
import com.projects.vehicle_renting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.RENTER);
        user.setWalletBalance(BigDecimal.ZERO);

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }
}
