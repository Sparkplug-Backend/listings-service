package com.sparkplug.listings.domain.repository;

import com.sparkplug.listings.domain.model.SavedSearch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SavedSearchRepository extends MongoRepository<SavedSearch, String> {
    List<SavedSearch> findByUserId(Long userId);
    void deleteByIdAndUserId(String id, Long userId);
} 