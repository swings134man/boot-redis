package com.boot.redis.lock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Redis Lock Test 
@SpringBootTest
class TestRedisServiceTest2 {

    @Autowired RedisLockService redisLockService;

    @Autowired RedisTemplate template;

    @Test
    @Transactional
    void lockTask() throws InterruptedException {
        String lockKey = "Key123";


    }


    void runLock(String key) {

    }

}