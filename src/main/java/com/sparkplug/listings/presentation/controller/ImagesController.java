package com.sparkplug.listings.presentation.controller;

import com.sparkplug.listings.application.dto.ImageOrderRequest;
import com.sparkplug.listings.application.port.ImagesServicePort;
import com.sparkplug.listings.infrastructure.security.aop.CheckListingOwnership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImagesController {

    private final ImagesServicePort imagesService;

    @Autowired
    public ImagesController(ImagesServicePort imagesService) {
        this.imagesService = imagesService;
    }

    @PutMapping("/reorder")
    @CheckListingOwnership
    public ResponseEntity<Void> reorderImages(
            @RequestParam("listingId") Long listingId,
            @RequestBody ImageOrderRequest[] imageOrders
    ) {

        Map<Long, Integer> map = new HashMap<>();
        for (var entry : imageOrders) {
            map.put(entry.id(), entry.order());
        }

        imagesService.reorderImages(listingId, map);

        return ResponseEntity.ok().build();
    }
}
