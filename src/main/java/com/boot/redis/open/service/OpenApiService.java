package com.boot.redis.open.service;

import com.boot.redis.config.util.RandomStringGen;
import com.boot.redis.open.OpenApi;
import com.boot.redis.open.repository.OpenApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiService {

    private final OpenApiRepository openApiRepository;

    // API KEY - GET
    public OpenApi getApiKey(String apiKey) {
        log.info("API Key (service): {}", apiKey);
        return openApiRepository.findByApiKey(apiKey);
    }

    public String genKey() {
        return genApiKey();
    }



    private String genApiKey() {
//        RandomStringGen randomStringGen = new RandomStringGen();
//        return randomStringGen.generateRandomKey();
        return RandomStringUtils.randomAlphanumeric(20); // Eng(Upper, Lower) + Number
    }

}
