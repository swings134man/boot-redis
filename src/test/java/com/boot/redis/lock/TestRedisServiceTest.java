package com.boot.redis.lock;

import com.boot.redis.about_redis.lock.RedisLockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Redis Lock Test 
@SpringBootTest
class TestRedisServiceTest {


    @Autowired
    RedisLockService redisLockService;

    @Test
    void lockTask() throws InterruptedException {
        String lockKey = "theLock";
        long expireTime = 5000L;

        int threadCount = 5;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService excutor = Executors.newFixedThreadPool(threadCount);


        for (int i = 0; i < threadCount; i++) {
            excutor.execute(() -> {
                String requestId = UUID.randomUUID().toString();

                // 1. Lock 획득 시도
                boolean isLock = redisLockService.acquireLock(lockKey, requestId, expireTime);
                if(isLock){
                    this.runLock(lockKey, requestId);
                }else {
                    System.out.println("Redis Lock 획득 실패!" + Thread.currentThread().getName());

                    boolean retryLock = true;
                    while(retryLock) {
                        retryLock = redisLockService.acquireLockRetry(lockKey, requestId, expireTime);
                    }

                    if(!retryLock) { // false 일때 RUN
                        this.runLock(lockKey, requestId);
                    }
                }

                latch.countDown();
            });

        }
        latch.await();
        excutor.shutdown();

        System.out.println("Test 종료~");
    }

    void runLock (String lockKey, String requestId) {
        try {
            // TODO : Lock 획득 후 처리할 비즈니스 로직?
            System.out.println("1. Lock 획득  By Thread =" + Thread.currentThread().getName());

            // 2. Thread sleep
            Thread.sleep(3000);
            System.out.println("2. 로직 처리 완료~  By Thread = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // Lock 해제
            redisLockService.releaseLock(lockKey, requestId);
            System.out.println("3. Lock 삭제  By Thread = " +  Thread.currentThread().getName());
        }
    }
}