package com.sparkplug.listings.domain.repository;

import com.sparkplug.listings.domain.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ListingsRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    boolean existsByIdAndCreatorId(Long id, Long creatorId);

    @Query("SELECT l FROM Listing l WHERE " +
           "(:minPrice IS NULL OR l.price.amount >= :minPrice) AND " +
           "(:maxPrice IS NULL OR l.price.amount <= :maxPrice) AND " +
           "(:minMileage IS NULL OR l.mileage.value >= :minMileage) AND " +
           "(:maxMileage IS NULL OR l.mileage.value <= :maxMileage)")
    List<Listing> findByFilters(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minMileage") Integer minMileage,
            @Param("maxMileage") Integer maxMileage
    );

    List<Listing> findByCreatorId(Long creatorId);
}
