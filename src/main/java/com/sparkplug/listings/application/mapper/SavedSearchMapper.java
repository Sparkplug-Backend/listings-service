package com.sparkplug.listings.application.mapper;

import com.sparkplug.listings.application.dto.SavedSearchDto;
import com.sparkplug.listings.domain.model.SavedSearch;
import com.sparkplug.listings.domain.model.SavedSearchFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SavedSearchMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "filters", source = "filters")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "lastUsed", source = "lastUsed")
    SavedSearchDto toDto(SavedSearch savedSearch);

    @Mapping(target = "minPrice", source = "minPrice")
    @Mapping(target = "maxPrice", source = "maxPrice")
    @Mapping(target = "minMileage", source = "minMileage")
    @Mapping(target = "maxMileage", source = "maxMileage")
    @Mapping(target = "manufacturerId", source = "manufacturerId")
    @Mapping(target = "carModelId", source = "carModelId")
    @Mapping(target = "generationId", source = "generationId")
    @Mapping(target = "drivetrainType", source = "drivetrainType")
    @Mapping(target = "fuelType", source = "fuelType")
    @Mapping(target = "minHorsepower", source = "minHorsepower")
    @Mapping(target = "maxHorsepower", source = "maxHorsepower")
    @Mapping(target = "transmissionType", source = "transmissionType")
    SavedSearchFilter toDomain(SavedSearchDto.FilterDto filterDto);
} 