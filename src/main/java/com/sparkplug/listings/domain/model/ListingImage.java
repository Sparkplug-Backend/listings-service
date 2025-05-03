package com.sparkplug.listings.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "listing_image")
public class ListingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    private Listing listing;
    
    private String url;

    @Setter
    @Column(name = "image_order")
    private Integer order;

    ListingImage(Listing listing, String url, Integer order) {
        this.listing = listing;
        this.url = url;
        this.order = order;
    }

    protected ListingImage() {}
}