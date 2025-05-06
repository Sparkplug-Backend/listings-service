package com.sparkplug.listings.application.service;

import com.sparkplug.listings.domain.model.Listing;
import com.sparkplug.listings.domain.vo.Mileage;
import com.sparkplug.listings.domain.vo.Price;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

@Transactional
@Testcontainers
@ActiveProfiles("test")
@DataJpaTest
public class ServiceTest {

    @Autowired
    TransactionTemplate transactionTemplate;
    
    @Autowired
    EntityManager em;

    protected Long insertListing() {
        return transactionTemplate.execute(status -> {
            var listing = Listing.builder()
                    .carConfigurationId(1L)
                    .creatorId(1L)
                    .price(new Price(BigDecimal.valueOf(50000)))
                    .mileage(new Mileage(10000))
                    .description("Test description")
                    .build();

            listing.setImages(List.of("https://test.com/image.jpg"));

            em.persist(listing);

            return listing.getId();
        });
    }
} 