package com.sparkplug.listings.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ListingDto(
    Long id,
    CarConfigurationDto carConfiguration,
    BigDecimal price,
    Integer mileage,
    String description,
    List<ListingImageDto> images,
    Long creatorId,
    LocalDateTime createdAt
) {}