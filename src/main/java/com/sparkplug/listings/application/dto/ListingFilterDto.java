package com.sparkplug.listings.application.dto;

import java.math.BigDecimal;

public record ListingFilterDto(
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
) {
    public static ListingFilterDtoBuilder builder() {
        return new ListingFilterDtoBuilder();
    }

    public static class ListingFilterDtoBuilder {
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

        public ListingFilterDtoBuilder minPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public ListingFilterDtoBuilder maxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public ListingFilterDtoBuilder minMileage(Integer minMileage) {
            this.minMileage = minMileage;
            return this;
        }

        public ListingFilterDtoBuilder maxMileage(Integer maxMileage) {
            this.maxMileage = maxMileage;
            return this;
        }

        public ListingFilterDtoBuilder manufacturerId(Long manufacturerId) {
            this.manufacturerId = manufacturerId;
            return this;
        }

        public ListingFilterDtoBuilder carModelId(Long carModelId) {
            this.carModelId = carModelId;
            return this;
        }

        public ListingFilterDtoBuilder generationId(Long generationId) {
            this.generationId = generationId;
            return this;
        }

        public ListingFilterDtoBuilder drivetrainType(String drivetrainType) {
            this.drivetrainType = drivetrainType;
            return this;
        }

        public ListingFilterDtoBuilder fuelType(String fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public ListingFilterDtoBuilder minHorsepower(Integer minHorsepower) {
            this.minHorsepower = minHorsepower;
            return this;
        }

        public ListingFilterDtoBuilder maxHorsepower(Integer maxHorsepower) {
            this.maxHorsepower = maxHorsepower;
            return this;
        }

        public ListingFilterDtoBuilder transmissionType(String transmissionType) {
            this.transmissionType = transmissionType;
            return this;
        }

        public ListingFilterDto build() {
            return new ListingFilterDto(
                minPrice, maxPrice, minMileage, maxMileage,
                manufacturerId, carModelId, generationId,
                drivetrainType, fuelType, minHorsepower,
                maxHorsepower, transmissionType
            );
        }
    }
} 