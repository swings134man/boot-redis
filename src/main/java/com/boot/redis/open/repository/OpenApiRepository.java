package com.boot.redis.open.repository;

import com.boot.redis.open.OpenApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiRepository extends JpaRepository<OpenApi, Long> {

    OpenApi findByApiKey(String apiKey);

    @Modifying
    @Query("update OpenApi o set o.apiKey = ?2 where o.apiMngrId = ?1")
    int reissueApiKey(Long idString, String newApiKey);
}
