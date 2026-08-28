package com.projects.vehicle_renting.controller;

import com.projects.vehicle_renting.dto.ApiResponse;
import com.projects.vehicle_renting.dto.RegisterRequest;
import com.projects.vehicle_renting.dto.UserResponse;
import com.projects.vehicle_renting.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

}
