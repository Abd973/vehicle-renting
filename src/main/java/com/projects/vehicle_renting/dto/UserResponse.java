package com.projects.vehicle_renting.dto;

import com.projects.vehicle_renting.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String name;
    private String phone;
    private Role role;
    private BigDecimal walletBalance;
}