package com.boot.redis.config.redis_config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RandomRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public String createCode() {
        return UUID.randomUUID().toString();
    }

    public void hasDelete(String key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    public String getKey(String key) {
        String result = "";
        if (redisTemplate.hasKey(key)) {
            result = redisTemplate.opsForValue().get(key);
        }
        return result;
    }

    public void save(Integer timeoutMinutes, String key, String value) {
        redisTemplate.opsForValue().set(key, value, timeoutMinutes, TimeUnit.MINUTES);
    }

    public boolean equals(String key, String value) {
        try {
            String savedValue = redisTemplate.opsForValue().get(key);
            return StringUtils.isNotEmpty(savedValue) && StringUtils.equals(savedValue, value);
        } catch (Exception e) {
            return false;
        }
    }
}
