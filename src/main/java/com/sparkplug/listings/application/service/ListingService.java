package com.sparkplug.listings.application.service;

import com.sparkplug.common.exception.ResourceNotFoundException;
import com.sparkplug.listings.application.dto.CarConfigurationDto;
import com.sparkplug.listings.application.dto.ListingDto;
import com.sparkplug.listings.application.dto.ListingFilterDto;
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
import java.util.Set;
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
        var carConfiguration = catalogPort.getConfiguration(dto.carModificationId());

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
    public ListingDto getListing(Long id) {
        var listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id));

        return toDto(listing, catalogPort.getConfiguration(listing.getCarConfigurationId()));
    }

    @Override
    public List<ListingDto> getListings() {
        return getListingsWithFilters(null);
    }

    @Override
    public List<ListingDto> getListingsByCreatorId(Long creatorId) {
        var listings = listingRepository.findByCreatorId(creatorId);
        var configurationIds = getConfigurationIds(listings);
        var carConfigurations = getConfigurationsFromCatalog(configurationIds);

        return mapListingsToDtos(listings, carConfigurations);
    }

    @Override
    public List<ListingDto> getListingsWithFilters(ListingFilterDto filter) {
        // First, get listings filtered by price and mileage
        var listings = filter != null ? 
            listingRepository.findByFilters(
                filter.minPrice(),
                filter.maxPrice(),
                filter.minMileage(),
                filter.maxMileage()
            ) : 
            listingRepository.findAll();

        // Get all car configurations for these listings
        var configurationIds = getConfigurationIds(listings);
        var carConfigurations = getConfigurationsFromCatalog(configurationIds);
        
        // Apply additional filters based on car configuration
        return mapListingsToDtos(listings, carConfigurations).stream()
            .filter(dto -> filter == null || matchesCarConfigurationFilter(dto.carConfiguration(), filter))
            .toList();
    }

    private Set<Long> getConfigurationIds(List<Listing> listings) {
        return listings.stream()
            .map(Listing::getCarConfigurationId)
            .collect(Collectors.toSet());
    }

    private Set<CarConfigurationDto> getConfigurationsFromCatalog(Set<Long> configurationIds) {
        return catalogPort.getConfigurations(configurationIds);
    }

    private List<ListingDto> mapListingsToDtos(List<Listing> listings, Set<CarConfigurationDto> configuration) {
        Map<Long, CarConfigurationDto> carConfigurations = configuration.stream()
            .collect(Collectors.toMap(CarConfigurationDto::id, mod -> mod));

        return listings.stream()
            .map(listing -> toDto(listing, carConfigurations.get(listing.getCarConfigurationId())))
            .toList();
    }


    private ListingDto toDto(Listing listing, CarConfigurationDto carConfiguration) {
        return listingMapper.toDto(listing, carConfiguration);
    }

    private boolean matchesCarConfigurationFilter(CarConfigurationDto carConfig, ListingFilterDto filter) {
        if (filter == null) {
            return true;
        }

        return (filter.manufacturerId() == null || carConfig.manufacturer().id().equals(filter.manufacturerId())) &&
               (filter.carModelId() == null || carConfig.model().id().equals(filter.carModelId())) &&
               (filter.generationId() == null || carConfig.generation().id().equals(filter.generationId())) &&
               (filter.drivetrainType() == null || carConfig.drivetrain().type().equals(filter.drivetrainType())) &&
               (filter.fuelType() == null || carConfig.engine().fuelType().equals(filter.fuelType())) &&
               (filter.minHorsepower() == null || carConfig.engine().horsepower() >= filter.minHorsepower()) &&
               (filter.maxHorsepower() == null || carConfig.engine().horsepower() <= filter.maxHorsepower()) &&
               (filter.transmissionType() == null || carConfig.transmission().type().equals(filter.transmissionType()));
    }
}