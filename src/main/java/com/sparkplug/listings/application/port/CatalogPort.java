package com.sparkplug.listings.application.port;

import com.sparkplug.listings.application.dto.CarConfigurationDto;

import java.util.Set;

public interface CatalogPort {
    CarConfigurationDto getConfiguration(Long id);
    Set<CarConfigurationDto> getConfigurations(Set<Long> ids);
}