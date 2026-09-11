package com.projects.vehicle_renting.repository;

import com.projects.vehicle_renting.model.Car;
import com.projects.vehicle_renting.model.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByStatusAndAvailable(CarStatus status, boolean available);
    List<Car> findAllByOwnerId(Long ownerId);
    List<Car> findAllByStatus(CarStatus status);
    boolean existsByVin(String vin);
}
