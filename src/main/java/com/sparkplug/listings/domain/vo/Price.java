package com.sparkplug.listings.domain.vo;

import com.sparkplug.listings.domain.exception.InvalidListingException;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Price(BigDecimal amount) {

    public Price{
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidListingException("Price must be greater than zero");
        }
    }

}