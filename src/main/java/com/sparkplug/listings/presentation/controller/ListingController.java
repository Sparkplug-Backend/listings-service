package com.sparkplug.listings.presentation.controller;

import com.sparkplug.commonauthentication.user.SparkplugUserDetails;
import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.application.port.ListingServicePort;
import com.sparkplug.listings.application.dto.CreateListingDto;
import com.sparkplug.listings.application.dto.ListingFilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ListingController {

    private final ListingServicePort listingService;

    @Autowired
    ListingController(ListingServicePort listingService) {
        this.listingService = listingService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ListingDto> createListing(
            @RequestPart("listing") CreateListingDto dto,
            @RequestPart("images") List<MultipartFile> images,
            @AuthenticationPrincipal SparkplugUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                listingService.createListing(dto, images, userDetails.getId())
        );
    }

    @GetMapping
    public ResponseEntity<List<ListingDto>> getListings(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minMileage,
            @RequestParam(required = false) Integer maxMileage,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) Long carModelId,
            @RequestParam(required = false) Long generationId,
            @RequestParam(required = false) String drivetrainType,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) Integer minHorsepower,
            @RequestParam(required = false) Integer maxHorsepower,
            @RequestParam(required = false) String transmissionType
    ) {
        var filter = ListingFilterDto.builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minMileage(minMileage)
                .maxMileage(maxMileage)
                .manufacturerId(manufacturerId)
                .carModelId(carModelId)
                .generationId(generationId)
                .drivetrainType(drivetrainType)
                .fuelType(fuelType)
                .minHorsepower(minHorsepower)
                .maxHorsepower(maxHorsepower)
                .transmissionType(transmissionType)
                .build();

        return ResponseEntity.ok(
            listingService.getListingsWithFilters(filter)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListing(@PathVariable Long id) {
        return ResponseEntity.ok(
            listingService.getListing(id)
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ListingDto>> getListingsByCreatorId(@PathVariable Long id) {
        return ResponseEntity.ok(
            listingService.getListingsByCreatorId(id)
        );
    }
}