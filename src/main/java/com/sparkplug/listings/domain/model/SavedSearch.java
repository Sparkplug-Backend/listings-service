package com.sparkplug.listings.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "saved_searches")
public class SavedSearch {
    @Id
    private String id;
    private Long userId;
    private String name;
    private SavedSearchFilter filters;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsed;
} 