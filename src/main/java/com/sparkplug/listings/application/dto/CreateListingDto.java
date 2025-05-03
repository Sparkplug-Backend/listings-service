package com.sparkplug.listings.application.dto;

import java.math.BigDecimal;

public record CreateListingDto(
    Long carModificationId,
    BigDecimal price,
    Integer mileage,
    String description
) {}