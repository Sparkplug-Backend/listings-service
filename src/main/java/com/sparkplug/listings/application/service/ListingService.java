package com.sparkplug.listings.application.service;

import com.sparkplug.common.exception.ResourceNotFoundException;
import com.sparkplug.listings.application.dto.CarModificationDto;
import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.application.mapper.ListingMapper;
import com.sparkplug.listings.application.port.CatalogPort;
import com.sparkplug.listings.application.port.ListingServicePort;
import com.sparkplug.listings.application.port.StoragePort;
import com.sparkplug.listings.domain.model.Listing;
import com.sparkplug.listings.domain.repository.ListingsRepository;
import com.sparkplug.listings.domain.vo.Mileage;
import com.sparkplug.listings.domain.vo.Price;
import com.sparkplug.listings.application.dto.CreateListingDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ListingService implements ListingServicePort {
    private final ListingsRepository listingRepository;
    private final StoragePort storagePort;
    private final CatalogPort catalogPort;
    private final ListingMapper listingMapper;

    ListingService(
            ListingsRepository listingRepository,
            StoragePort storagePort,
            CatalogPort catalogPort,
            ListingMapper listingMapper
    ) {
        this.listingRepository = listingRepository;
        this.storagePort = storagePort;
        this.catalogPort = catalogPort;
        this.listingMapper = listingMapper;
    }

    @Override
    @Transactional
    public ListingDto createListing(CreateListingDto dto, List<MultipartFile> images, Long userId) {

        var carConfiguration = catalogPort.getModification(dto.carModificationId());

        var listing = Listing.builder()
                .carConfigurationId(dto.carModificationId())
                .creatorId(userId)
                .price(new Price(dto.price()))
                .mileage(new Mileage(dto.mileage()))
                .description(dto.description())
                .build();

        var imageUrls = storagePort.uploadFiles(images);
        listing.setImages(imageUrls);

        listingRepository.save(listing);

        return toDto(listing, carConfiguration);
    }

    @Override
    public List<ListingDto> getListings() {

        var listings = listingRepository.findAll();

        var configurationIds = listings.stream()
            .map(Listing::getCarConfigurationId)
            .collect(Collectors.toSet());

        Map<Long, CarModificationDto> carConfigurations = catalogPort.getModifications(configurationIds).stream()
                .collect(Collectors.toMap(CarModificationDto::id, config -> config));

        return listings.stream()
            .map(listing -> {
                var carConfig = carConfigurations.get(listing.getCarConfigurationId());
                return toDto(listing, carConfig);
            })
            .toList();
    }

    @Override
    public ListingDto getListing(Long id) {

        var listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id));

        return toDto(listing, catalogPort.getModification(listing.getCarConfigurationId()));
    }

    private ListingDto toDto(Listing listing, CarModificationDto carConfiguration) {
        return listingMapper.toDto(listing, carConfiguration);
    }
}