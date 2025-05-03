package com.sparkplug.listings;

import com.sparkplug.listings.infrastructure.environment.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ListingsApplication {

    public static void main(String[] args) {
        EnvLoader.load();
        SpringApplication.run(ListingsApplication.class, args);
    }

}
