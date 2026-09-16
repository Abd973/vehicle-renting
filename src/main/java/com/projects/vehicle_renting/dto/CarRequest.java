package com.projects.vehicle_renting.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequest {

    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    private String vin;

    @NotBlank(message = "Brand name is required")
    private String brandName;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Base price per day is required")
    @DecimalMin(value = "0.01", message = "Base price per day must be greater than zero")
    private BigDecimal basePricePerDay;
}