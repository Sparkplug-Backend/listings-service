package com.sparkplug.listings.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SavedSearchDto(
    String id,
    Long userId,
    String name,
    FilterDto filters,
    LocalDateTime createdAt,
    LocalDateTime lastUsed
) {
    public record FilterDto(
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minMileage,
        Integer maxMileage,
        Long manufacturerId,
        Long carModelId,
        Long generationId,
        String drivetrainType,
        String fuelType,
        Integer minHorsepower,
        Integer maxHorsepower,
        String transmissionType
    ) {}
} 