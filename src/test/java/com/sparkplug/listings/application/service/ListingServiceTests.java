package com.sparkplug.listings.application.service;

import com.sparkplug.common.exception.ResourceNotFoundException;
import com.sparkplug.listings.application.dto.CarConfigurationDto;
import com.sparkplug.listings.application.dto.CreateListingDto;
import com.sparkplug.listings.application.mapper.ListingMapperImpl;
import com.sparkplug.listings.application.port.CatalogPort;
import com.sparkplug.listings.application.port.StoragePort;
import com.sparkplug.listings.domain.model.Listing;
import com.sparkplug.listings.domain.repository.ListingsRepository;
import com.sparkplug.listings.domain.vo.Mileage;
import com.sparkplug.listings.domain.vo.Price;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@Import({ListingService.class, ListingMapperImpl.class})
class ListingServiceTests extends ServiceTest {

    @Autowired
    private ListingService listingService;

    @Autowired
    private ListingsRepository listingsRepository;

    @MockitoBean
    private CatalogPort catalogPort;

    @MockitoBean
    private StoragePort storagePort;

    private final Long configurationId = 1L;

    private final CarConfigurationDto configuration = new CarConfigurationDto(
            configurationId,
            "Test Modification",
            new CarConfigurationDto.Engine("Petrol", "V8", 400, 500),
            new CarConfigurationDto.Transmission("Automatic", 8),
            new CarConfigurationDto.Drivetrain("AWD"),
            new CarConfigurationDto.Generation(1L, "Test Generation", 2020),
            new CarConfigurationDto.Model(1L, "Test Model"),
            new CarConfigurationDto.Manufacturer(1L, "Test Manufacturer", "Test Country")
    );

    @Test
    void contextLoads() {
        assertNotNull(listingService);
    }

    @Test
    void createListing_shouldReturnDto() {
        // Given
        var userId = 1L;
        var price = BigDecimal.valueOf(50000);
        var mileage = 10000;
        var description = "Test description";
        var imageUrl = "http://test.com/image.jpg";
        
        var dto = new CreateListingDto(configurationId, price, mileage, description);
        var image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());
        
        when(catalogPort.getConfiguration(configurationId)).thenReturn(configuration);
        when(storagePort.uploadFiles(any())).thenReturn(List.of(imageUrl));

        // When
        var result = listingService.createListing(dto, List.of(image), userId);

        // Then
        assertNotNull(result);
        assertEquals(configurationId, result.carConfiguration().id());
        assertEquals(price, result.price());
        assertEquals(mileage, result.mileage());
        assertEquals(description, result.description());
        assertEquals(1, result.images().size());
    }

    @Test
    void getListings_whenNotEmpty_shouldReturnList() {
        // Given
        var carModificationId = 1L;
        var userId = 1L;
        var listing = Listing.builder()
                .carConfigurationId(carModificationId)
                .creatorId(userId)
                .price(new Price(BigDecimal.valueOf(50000)))
                .mileage(new Mileage(10000))
                .description("Test description")
                .build();
        
        listing.setImages(List.of("https://test.com/image.jpg"));
        listingsRepository.save(listing);

        when(catalogPort.getConfigurations(anySet())).thenReturn(Set.of(configuration));

        // When
        var result = listingService.getListings();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(carModificationId, result.get(0).carConfiguration().id());
    }

    @Test
    void getListing_whenExists_shouldReturnDto() {
        // Given
        var carModificationId = 1L;
        var userId = 1L;
        var listing = Listing.builder()
                .carConfigurationId(carModificationId)
                .creatorId(userId)
                .price(new Price(BigDecimal.valueOf(50000)))
                .mileage(new Mileage(10000))
                .description("Test description")
                .build();
        
        listing.setImages(List.of("http://test.com/image.jpg"));
        var savedListing = listingsRepository.save(listing);

        when(catalogPort.getConfiguration(carModificationId)).thenReturn(configuration);

        // When
        var result = listingService.getListing(savedListing.getId());

        // Then
        assertNotNull(result);
        assertEquals(savedListing.getId(), result.id());
        assertEquals(carModificationId, result.carConfiguration().id());
    }

    @Test
    void getListing_whenDoesNotExist_shouldThrow() {
        // Given
        var nonExistentId = 999L;

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> listingService.getListing(nonExistentId));
    }
} 