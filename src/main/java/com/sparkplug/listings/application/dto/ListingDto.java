package com.sparkplug.listings.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ListingDto(
    Long id,
    CarModificationDto carModification,
    BigDecimal price,
    Integer mileage,
    String description,
    List<ListingImageDto> images,
    Long creatorId,
    LocalDateTime createdAt
) {}