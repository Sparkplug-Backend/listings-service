package com.sparkplug.listings.application.port;

import com.sparkplug.listings.domain.model.SavedSearch;
import com.sparkplug.listings.domain.model.SavedSearchFilter;

import java.util.List;

public interface SavedSearchServicePort {
    SavedSearch saveSearch(Long userId, String name, SavedSearchFilter filters);
    List<SavedSearch> getUserSearches(Long userId);
    void deleteSearch(String id, Long userId);
    void updateLastUsed(String id);
} 