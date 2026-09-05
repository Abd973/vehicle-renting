package com.projects.vehicle_renting.service;

import com.projects.vehicle_renting.dto.BrandDTO;
import com.projects.vehicle_renting.exception.ConflictException;
import com.projects.vehicle_renting.exception.ResourceNotFoundException;
import com.projects.vehicle_renting.mapper.BrandMapper;
import com.projects.vehicle_renting.model.Brand;
import com.projects.vehicle_renting.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandDTO create(BrandDTO request) {
        if (brandRepository.existsByName(request.getName())) {
            throw new ConflictException("Brand already exists with name: " + request.getName());
        }

        Brand brand = brandMapper.toEntity(request);
        Brand savedBrand = brandRepository.save(brand);

        return brandMapper.toDTO(savedBrand);
    }

    public BrandDTO update(Long id, BrandDTO request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        if (request.getName() != null
                && !request.getName().equalsIgnoreCase(brand.getName())
                && brandRepository.existsByName(request.getName())) {
            throw new ConflictException("Brand already exists with name: " + request.getName());
        }

        brand.setName(request.getName());
        brand.setDescription(request.getDescription());

        Brand updatedBrand = brandRepository.save(brand);

        return brandMapper.toDTO(updatedBrand);
    }

    public BrandDTO getById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        return brandMapper.toDTO(brand);
    }

    public List<BrandDTO> getAll() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toDTO)
                .toList();
    }

    public void delete(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        brandRepository.delete(brand);
    }
}