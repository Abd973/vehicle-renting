package com.projects.vehicle_renting.mapper;

import com.projects.vehicle_renting.dto.BrandDTO;
import com.projects.vehicle_renting.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Brand toEntity(BrandDTO dto);

    BrandDTO toDTO(Brand brand);
}