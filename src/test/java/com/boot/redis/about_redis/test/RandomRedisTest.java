package com.boot.redis.about_redis.test;

import com.boot.redis.config.redis_config.RandomRedisService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class RandomRedisTest {

    @Autowired
    RandomRedisService randomRedisService;

    @DisplayName("1. Service Test")
    @Test
    @Rollback
    void test() {
        String code = randomRedisService.createCode();
        String key = "test";
        randomRedisService.save(30, key, code);

        assert randomRedisService.equals(key, code);

        String res = randomRedisService.getKey(key);
        System.out.println("res = " + res);

        randomRedisService.hasDelete(key);
        Assertions.assertThat(randomRedisService.getKey(key)).isEmpty();
    }
}
