package com.sparkplug.listings.application.dto;

public record CarModificationDto(

        Long id,
        String name,

        Engine engine,
        Transmission transmission,
        Drivetrain drivetrain,
        Generation generation,
        Model model,
        Manufacturer manufacturer
) {
    public record Engine(
            String fuelType,
            String type,
            Integer horsepower,
            Integer torque
    ) {}

    public record Transmission(
            String type,
            Integer numberOfGears
    ) {}

    public record Generation(
            Long id,
            String name,
            Integer startYear
    ) {}

    public record Model(
            Long id,
            String name
    ) {}

    public record Manufacturer(
            Long id,
            String name,
            String country
    ) {}

    public record Drivetrain(
            String type
    ) {}
}
