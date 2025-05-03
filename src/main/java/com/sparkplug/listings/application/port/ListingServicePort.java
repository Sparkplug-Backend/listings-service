package com.sparkplug.listings.application.port;

import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.application.dto.CreateListingDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingServicePort {
    ListingDto createListing(CreateListingDto dto, List<MultipartFile> images, Long userId);
    List<ListingDto> getListings();
    ListingDto getListing(Long id);
}