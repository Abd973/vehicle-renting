package com.projects.vehicle_renting.controller;

import com.projects.vehicle_renting.dto.ApiResponse;
import com.projects.vehicle_renting.dto.CarRequest;
import com.projects.vehicle_renting.dto.CarResponse;
import com.projects.vehicle_renting.dto.CarUpdateRequest;
import com.projects.vehicle_renting.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'COMPANY', 'ADMIN')")
    public ResponseEntity<ApiResponse<CarResponse>> create(@Valid @RequestBody CarRequest carRequest) {
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        CarResponse car = carService.create(ownerEmail, carRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("The car created successfully", car));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('OWNER', 'COMPANY', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getMyCars() {
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CarResponse> cars = carService.getMyCars(ownerEmail);
        return ResponseEntity.ok(ApiResponse.success("My cars", cars));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAvailableCars() {
        List<CarResponse> cars = carService.getAvailableCars();
        return ResponseEntity.ok(ApiResponse.success("Available cars", cars));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getPendingCars() {
        List<CarResponse> cars = carService.getPendingCars();
        return ResponseEntity.ok(ApiResponse.success("Pending cars", cars));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approveCar(@PathVariable Long id) {
        carService.approveCar(id);
        return ResponseEntity.ok(ApiResponse.success("Car approved successfully"));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectCar(@PathVariable Long id) {
        carService.rejectCar(id);
        return ResponseEntity.ok(ApiResponse.success("Car rejected successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'COMPANY', 'ADMIN')")
    public ResponseEntity<ApiResponse<CarResponse>> update(@PathVariable Long id,
                                                           @Valid @RequestBody CarUpdateRequest carUpdateRequest) {
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        CarResponse car = carService.update(id, ownerEmail, carUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success("The car updated successfully", car));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'COMPANY', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        carService.delete(id, ownerEmail);
        return ResponseEntity.ok(ApiResponse.success("Car deleted successfully"));
    }
}