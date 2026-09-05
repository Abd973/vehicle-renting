package com.projects.vehicle_renting.repository;


import com.projects.vehicle_renting.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
}
