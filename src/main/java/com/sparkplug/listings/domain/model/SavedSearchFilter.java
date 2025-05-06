package com.sparkplug.listings.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SavedSearchFilter {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minMileage;
    private Integer maxMileage;
    private Long manufacturerId;
    private Long carModelId;
    private Long generationId;
    private String drivetrainType;
    private String fuelType;
    private Integer minHorsepower;
    private Integer maxHorsepower;
    private String transmissionType;
} 