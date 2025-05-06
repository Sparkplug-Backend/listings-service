package com.sparkplug.listings.application.service;

import com.sparkplug.listings.application.port.SavedSearchServicePort;
import com.sparkplug.listings.domain.model.SavedSearch;
import com.sparkplug.listings.domain.model.SavedSearchFilter;
import com.sparkplug.listings.domain.repository.SavedSearchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SavedSearchService implements SavedSearchServicePort {
    private final SavedSearchRepository repository;

    public SavedSearchService(SavedSearchRepository repository) {
        this.repository = repository;
    }

    @Override
    public SavedSearch saveSearch(Long userId, String name, SavedSearchFilter filters) {
        var savedSearch = SavedSearch.builder()
                .userId(userId)
                .name(name)
                .filters(filters)
                .createdAt(LocalDateTime.now())
                .lastUsed(LocalDateTime.now())
                .build();

        return repository.save(savedSearch);
    }

    @Override
    public List<SavedSearch> getUserSearches(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void deleteSearch(String id, Long userId) {
        repository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public void updateLastUsed(String id) {
        repository.findById(id).ifPresent(search -> {
            search.setLastUsed(LocalDateTime.now());
            repository.save(search);
        });
    }
} 