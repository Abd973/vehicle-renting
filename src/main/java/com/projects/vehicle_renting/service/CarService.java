package com.projects.vehicle_renting.service;


import com.projects.vehicle_renting.dto.CarRequest;
import com.projects.vehicle_renting.dto.CarResponse;
import com.projects.vehicle_renting.dto.CarUpdateRequest;
import com.projects.vehicle_renting.exception.BadRequestException;
import com.projects.vehicle_renting.exception.ConflictException;
import com.projects.vehicle_renting.exception.ForbiddenException;
import com.projects.vehicle_renting.exception.ResourceNotFoundException;
import com.projects.vehicle_renting.mapper.CarMapper;
import com.projects.vehicle_renting.model.Brand;
import com.projects.vehicle_renting.model.Car;
import com.projects.vehicle_renting.model.User;
import com.projects.vehicle_renting.model.enums.CarStatus;
import com.projects.vehicle_renting.repository.BrandRepository;
import com.projects.vehicle_renting.repository.CarRepository;
import com.projects.vehicle_renting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CarMapper carMapper;


    public CarResponse create(String ownerEmail, CarRequest carRequest) {
        if (carRepository.existsByVin(carRequest.getVin())) {
            throw new ConflictException("Car already exists with VIN: " + carRequest.getVin());
        }
        Brand brand = brandRepository.findById(carRequest.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", carRequest.getBrandId()));
        User owner = resolveOwner(ownerEmail);
        Car car = carMapper.toEntity(carRequest);
        car.setBrand(brand);
        car.setOwner(owner);
        carRepository.save(car);
        return carMapper.toResponse(car);
    }

    public List<CarResponse> getMyCars(String ownerEmail) {
        User owner = resolveOwner(ownerEmail);
        return carRepository.findAllByOwnerId(owner.getId())
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }

    public List<CarResponse> getAvailableCars() {
        return carRepository.findAllByStatusAndAvailable(CarStatus.APPROVED, true)
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }

    public List<CarResponse> getPendingCars() {
        return carRepository.findAllByStatus(CarStatus.PENDING)
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }

    public void approveCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId));
        if (car.getStatus() == CarStatus.APPROVED) {
            throw new BadRequestException("Car is already approved");
        }
        car.setStatus(CarStatus.APPROVED);
        carRepository.save(car);
    }

    public void rejectCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId));
        if (car.getStatus() == CarStatus.REJECTED) {
            throw new BadRequestException("Car is already rejected");
        }
        car.setStatus(CarStatus.REJECTED);
        carRepository.save(car);
    }

    public CarResponse update(Long carId, String ownerEmail, CarUpdateRequest carUpdateRequest) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId));
        User owner = resolveOwner(ownerEmail);

        if (!car.getOwner().getId().equals(owner.getId())) {
            throw new ForbiddenException("You can only update your own cars");
        }

        boolean priceChanged = carUpdateRequest.getBasePricePerDay() != null
                && carUpdateRequest.getBasePricePerDay().compareTo(car.getBasePricePerDay()) != 0;

        if (priceChanged) {
            car.setBasePricePerDay(carUpdateRequest.getBasePricePerDay());
        }

        if (carUpdateRequest.getAvailable() != null) {
            car.setAvailable(carUpdateRequest.getAvailable());
        }

        carRepository.save(car);
        return carMapper.toResponse(car);
    }

    public void delete(Long carId, String ownerEmail) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId));
        User owner = resolveOwner(ownerEmail);

        if (!car.getOwner().getId().equals(owner.getId())) {
            throw new ForbiddenException("You can only delete your own cars");
        }

        carRepository.delete(car);
    }

    private User resolveOwner(String ownerEmail) {
        return userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", ownerEmail));
    }
}
