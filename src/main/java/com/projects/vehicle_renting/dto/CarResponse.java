package com.projects.vehicle_renting.dto;

import com.projects.vehicle_renting.model.enums.CarStatus;
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
public class CarResponse {

    private Long id;
    private String vin;
    private Long brandId;
    private String brandName;
    private String model;
    private BigDecimal basePricePerDay;
    private boolean available;
    private CarStatus status;
    private Long ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}