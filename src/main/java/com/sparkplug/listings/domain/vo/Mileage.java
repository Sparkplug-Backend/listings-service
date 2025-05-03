package com.sparkplug.listings.domain.vo;

import com.sparkplug.listings.domain.exception.InvalidListingException;
import jakarta.persistence.Embeddable;

@Embeddable
public record Mileage(Integer value) {

    public Mileage {
        if (value == null || value < 0) {
            throw new InvalidListingException("Mileage cannot be negative");
        }
    }
}