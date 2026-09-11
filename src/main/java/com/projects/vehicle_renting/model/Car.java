package com.projects.vehicle_renting.model;

import com.projects.vehicle_renting.model.enums.CarStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 17)
    private String vin;
    @ManyToOne
    private Brand brand;
    private String model;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private BigDecimal basePricePerDay;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CarStatus status = CarStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

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
