package com.projects.vehicle_renting.service;

import com.projects.vehicle_renting.dto.UserResponse;
import com.projects.vehicle_renting.exception.ResourceNotFoundException;
import com.projects.vehicle_renting.mapper.UserMapper;
import com.projects.vehicle_renting.model.User;
import com.projects.vehicle_renting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.toResponse(user);
    }
}