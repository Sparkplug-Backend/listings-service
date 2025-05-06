package com.sparkplug.listings.presentation.controller;

import com.sparkplug.commonauthentication.user.SparkplugUserDetails;
import com.sparkplug.listings.application.dto.SavedSearchDto;
import com.sparkplug.listings.application.mapper.SavedSearchMapper;
import com.sparkplug.listings.application.port.SavedSearchServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-searches")
public class SavedSearchController {
    private final SavedSearchServicePort savedSearchService;
    private final SavedSearchMapper mapper;

    @Autowired
    public SavedSearchController(SavedSearchServicePort savedSearchService, SavedSearchMapper mapper) {
        this.savedSearchService = savedSearchService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<SavedSearchDto> saveSearch(
            @AuthenticationPrincipal SparkplugUserDetails userDetails,
            @RequestParam String name,
            @RequestBody SavedSearchDto.FilterDto filterDto
    ) {
        var savedSearch = savedSearchService.saveSearch(
                userDetails.getId(),
                name,
                mapper.toDomain(filterDto)
        );
        return ResponseEntity.ok(mapper.toDto(savedSearch));
    }

    @GetMapping
    public ResponseEntity<List<SavedSearchDto>> getUserSearches(
            @AuthenticationPrincipal SparkplugUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                savedSearchService.getUserSearches(userDetails.getId())
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearch(
            @AuthenticationPrincipal SparkplugUserDetails userDetails,
            @PathVariable String id
    ) {
        savedSearchService.deleteSearch(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }
} 