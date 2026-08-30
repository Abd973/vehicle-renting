package com.projects.vehicle_renting.dto;

import com.projects.vehicle_renting.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
    private BigDecimal walletBalance;
    private LocalDateTime createdAt;
}
