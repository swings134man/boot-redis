package com.boot.redis.lock;

import com.boot.redis.about_redis.lock.RedisLockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;


// Redis Lock Test 
@SpringBootTest
class TestRedisServiceTest2 {

    @Autowired
    RedisLockService redisLockService;

    @Autowired RedisTemplate template;

    @Test
    @Transactional
    void lockTask() throws InterruptedException {
        String lockKey = "Key123";


    }


    void runLock(String key) {

    }

}