package com.sparkplug.listings.infrastructure.security.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.sparkplug.commonauthentication.contract.PublicKeyProvider;
import com.sparkplug.listings.infrastructure.feign.AuthFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.Map;

@Service
public class JwkService implements PublicKeyProvider {

    private final AuthFeignClient authFeignClient;

    @Autowired
    public JwkService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    @Cacheable("publicKey")
    @Override
    public PublicKey getPublicKey() {
        try {
            Map<String, Object> jwksResponse = authFeignClient.fetchJwk();

            return JWK.parse(jwksResponse).toRSAKey().toPublicKey();
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

    }
}