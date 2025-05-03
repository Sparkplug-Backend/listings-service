package com.sparkplug.listings.infrastructure.adapter;

import com.sparkplug.listings.application.dto.CarModificationDto;
import com.sparkplug.listings.application.port.CatalogPort;
import com.sparkplug.listings.infrastructure.feign.CatalogFeignClient;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CatalogAdapter implements CatalogPort {
    private final CatalogFeignClient catalogClient;

    public CatalogAdapter(CatalogFeignClient catalogClient) {
        this.catalogClient = catalogClient;
    }

    @Override
    public CarModificationDto getModification(Long id) {
        return catalogClient.getModification(id);
    }

    @Override
    public Set<CarModificationDto> getModifications(Set<Long> ids) {
        return catalogClient.getModifications(ids);
    }
}