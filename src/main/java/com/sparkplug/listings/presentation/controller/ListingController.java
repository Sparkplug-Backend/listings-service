package com.sparkplug.listings.presentation.controller;

import com.sparkplug.commonauthentication.user.SparkplugUserDetails;
import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.application.port.ListingServicePort;
import com.sparkplug.listings.application.dto.CreateListingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<ListingDto>> getListings() {
        return ResponseEntity.ok(
            listingService.getListings()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListing(@PathVariable Long id) {
        return ResponseEntity.ok(
            listingService.getListing(id)
        );
    }
}