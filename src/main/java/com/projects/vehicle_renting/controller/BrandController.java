package com.projects.vehicle_renting.controller;

import com.projects.vehicle_renting.dto.ApiResponse;
import com.projects.vehicle_renting.dto.BrandDTO;
import com.projects.vehicle_renting.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandDTO>>> getAll() {
        List<BrandDTO> brands = brandService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Brands fetched successfully", brands));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandDTO>> getById(@PathVariable Long id) {
        BrandDTO brand = brandService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Brand fetched successfully", brand));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BrandDTO>> create(@Valid @RequestBody BrandDTO request) {
        BrandDTO brand = brandService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Brand created successfully", brand));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BrandDTO>> update(
            @PathVariable Long id, @Valid @RequestBody BrandDTO request) {
        BrandDTO brand = brandService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Brand updated successfully", brand));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully"));
    }
}