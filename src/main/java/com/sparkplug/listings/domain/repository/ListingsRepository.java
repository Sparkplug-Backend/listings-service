package com.sparkplug.listings.domain.repository;

import com.sparkplug.listings.domain.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingsRepository extends JpaRepository<Listing, Long> {

    boolean existsByIdAndCreatorId(Long id, Long creatorId);
}
