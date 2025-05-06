package com.sparkplug.listings.application.port;

import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.application.dto.CreateListingDto;
import com.sparkplug.listings.application.dto.ListingFilterDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingServicePort {
    ListingDto createListing(CreateListingDto dto, List<MultipartFile> images, Long userId);
    List<ListingDto> getListings();
    List<ListingDto> getListingsWithFilters(ListingFilterDto filter);
    List<ListingDto> getListingsByCreatorId(Long creatorId);
    ListingDto getListing(Long id);
}