package com.boot.redis.open.repository;

import com.boot.redis.open.OpenApi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiRepository extends JpaRepository<OpenApi, Long> {

    OpenApi findByApiKey(String apiKey);
}
