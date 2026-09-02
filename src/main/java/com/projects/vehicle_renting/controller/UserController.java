package com.projects.vehicle_renting.controller;

import com.projects.vehicle_renting.dto.ApiResponse;
import com.projects.vehicle_renting.dto.UserResponse;
import com.projects.vehicle_renting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse userResponse = userService.getCurrentUser(email);
        return ResponseEntity.ok(ApiResponse.success("User profile", userResponse));
    }
}
