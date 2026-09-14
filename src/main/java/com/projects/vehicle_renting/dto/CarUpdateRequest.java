package com.projects.vehicle_renting.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarUpdateRequest {

    @DecimalMin(value = "0.01", message = "Base price per day must be greater than zero")
    private BigDecimal basePricePerDay;

    private Boolean available;
}