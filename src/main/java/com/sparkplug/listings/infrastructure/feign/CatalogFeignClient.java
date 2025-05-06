package com.sparkplug.listings.infrastructure.feign;

import com.sparkplug.listings.application.dto.CarConfigurationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient("CATALOG")
public interface CatalogFeignClient {

    @GetMapping("/modifications/detailed/{id}")
    CarConfigurationDto getModification(@PathVariable Long id);

    @GetMapping("/modifications/detailed/")
    Set<CarConfigurationDto> getModifications(@RequestParam Set<Long> ids);
}