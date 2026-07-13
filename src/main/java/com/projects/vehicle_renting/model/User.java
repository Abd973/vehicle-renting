package com.projects.vehicle_renting.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.projects.vehicle_renting.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private BigDecimal walletBalance;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    private void onUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
