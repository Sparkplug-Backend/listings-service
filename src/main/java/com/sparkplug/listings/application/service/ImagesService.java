package com.sparkplug.listings.application.service;

import com.sparkplug.common.exception.ResourceNotFoundException;
import com.sparkplug.listings.application.port.ImagesServicePort;
import com.sparkplug.listings.domain.repository.ListingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ImagesService implements ImagesServicePort {

    private final ListingsRepository listingsRepository;

    @Autowired
    public ImagesService(ListingsRepository listingsRepository) {
        this.listingsRepository = listingsRepository;
    }

    @Override
    public void reorderImages(Long listingId, Map<Long, Integer> idsAndOrders) {
        var listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", listingId));

        listing.reorderImages(idsAndOrders);

        listingsRepository.save(listing);
    }
}
