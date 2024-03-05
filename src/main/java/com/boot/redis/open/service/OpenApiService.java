package com.boot.redis.open.service;

import com.boot.redis.open.OpenApi;
import com.boot.redis.open.repository.OpenApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiService {

    private final OpenApiRepository openApiRepository;

    public OpenApi getApiKey(String apiKey) {
        log.info("API Key (service): {}", apiKey);
        return openApiRepository.findByApiKey(apiKey);
    }

}
