package com.sparkplug.listings.domain.model;

import com.sparkplug.listings.domain.vo.Mileage;
import com.sparkplug.listings.domain.vo.Price;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "listing")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_configuration_id")
    private Long carConfigurationId;

    @Column(name = "creator_id")
    private Long creatorId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Price price;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "mileage"))
    private Mileage mileage;
    
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "listing_id")
    @Builder.Default
    private List<ListingImage> images = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void setImages(List<String> imageUrls) {
        var count = imageUrls.size();

        var newImages = new ArrayList<ListingImage>(count);
        for (int i = 0; i < count; i++) {
            var url = imageUrls.get(i);
            newImages.add(new ListingImage(this, url, i));
        }

        this.images = newImages;
    }

    public void removeImages(List<Long> ids) {
        this.images.sort(Comparator.comparingInt(ListingImage::getOrder));
        this.images.removeIf(image -> ids.contains(image.getId()));
        this.images.forEach(image -> image.setOrder(this.images.indexOf(image)));
    }

    public void reorderImages(Map<Long, Integer> idsAndOrders) {
        if (idsAndOrders.size() != this.images.size()) {
            throw new IllegalArgumentException("The number of images provided does not match the current listing.");
        }

        // Validate that all IDs in idsAndOrders exist in this.images
        Set<Long> existingImageIds = this.images.stream()
                .map(ListingImage::getId)
                .collect(Collectors.toSet());
        if (!existingImageIds.containsAll(idsAndOrders.keySet())) {
            throw new IllegalArgumentException("One or more image IDs provided do not belong to this listing.");
        }

        // Validate that the order values are sequential and start from 0
        List<Integer> orderValues = new ArrayList<>(idsAndOrders.values());
        Collections.sort(orderValues);
        for (int i = 0; i < orderValues.size(); i++) {
            if (orderValues.get(i) != i) {
                throw new IllegalArgumentException("Invalid order values: Orders must be sequential starting from 0.");
            }
        }

        // Update the order of each image
        this.images.forEach(image -> {
            var newOrder = idsAndOrders.get(image.getId());
            if (newOrder == null) {
                throw new IllegalStateException("Unexpected error: Image ID not found in the provided order map.");
            }
            image.setOrder(newOrder);
        });
    }
}
