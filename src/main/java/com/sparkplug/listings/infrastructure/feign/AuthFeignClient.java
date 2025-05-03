package com.sparkplug.listings.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient("AUTH")
public interface AuthFeignClient {

    @GetMapping("/.well-known/jwks.json")
    Map<String, Object> fetchJwk();
}
