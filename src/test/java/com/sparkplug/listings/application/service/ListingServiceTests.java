package com.sparkplug.listings.application.service;

import com.sparkplug.common.exception.ResourceNotFoundException;
import com.sparkplug.listings.application.dto.CarModificationDto;
import com.sparkplug.listings.application.dto.CreateListingDto;
import com.sparkplug.listings.application.mapper.ListingMapperImpl;
import com.sparkplug.listings.application.port.CatalogPort;
import com.sparkplug.listings.application.port.StoragePort;
import com.sparkplug.listings.domain.model.Listing;
import com.sparkplug.listings.domain.repository.ListingsRepository;
import com.sparkplug.listings.domain.vo.Mileage;
import com.sparkplug.listings.domain.vo.Price;
import com.sparkplug.listings.service.ServiceTest;
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

    private final Long carModificationId = 1L;

    private final CarModificationDto carModification = new CarModificationDto(
            carModificationId,
            "Test Modification",
            new CarModificationDto.Engine("Petrol", "V8", 400, 500),
            new CarModificationDto.Transmission("Automatic", 8),
            new CarModificationDto.Drivetrain("AWD"),
            new CarModificationDto.Generation(1L, "Test Generation", 2020),
            new CarModificationDto.Model(1L, "Test Model"),
            new CarModificationDto.Manufacturer(1L, "Test Manufacturer", "Test Country")
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
        
        var dto = new CreateListingDto(carModificationId, price, mileage, description);
        var image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());
        
        when(catalogPort.getModification(carModificationId)).thenReturn(carModification);
        when(storagePort.uploadFiles(any())).thenReturn(List.of(imageUrl));

        // When
        var result = listingService.createListing(dto, List.of(image), userId);

        // Then
        assertNotNull(result);
        assertEquals(carModificationId, result.carModification().id());
        assertEquals(price, result.price());
        assertEquals(mileage, result.mileage());
        assertEquals(description, result.description());
        assertEquals(1, result.images().size());
        assertEquals(imageUrl, result.images().get(0));
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

        when(catalogPort.getModifications(anySet())).thenReturn(Set.of(carModification));

        // When
        var result = listingService.getListings();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(carModificationId, result.get(0).carModification().id());
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

        when(catalogPort.getModification(carModificationId)).thenReturn(carModification);

        // When
        var result = listingService.getListing(savedListing.getId());

        // Then
        assertNotNull(result);
        assertEquals(savedListing.getId(), result.id());
        assertEquals(carModificationId, result.carModification().id());
    }

    @Test
    void getListing_whenDoesNotExist_shouldThrow() {
        // Given
        var nonExistentId = 999L;

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> listingService.getListing(nonExistentId));
    }
} 