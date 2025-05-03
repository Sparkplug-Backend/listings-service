package com.sparkplug.listings.application.port;

import com.sparkplug.listings.application.dto.CarModificationDto;

import java.util.Set;

public interface CatalogPort {
    CarModificationDto getModification(Long id);
    Set<CarModificationDto> getModifications(Set<Long> ids);
}