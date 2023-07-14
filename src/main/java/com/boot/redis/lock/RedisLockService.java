package com.boot.redis.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/************
 * @info : Redis Lock Test Service
 * @name : RedisLockService
 * @date : 2023/07/14 10:55 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Redis Lock 테스트 설정을 위한 Service 클래스.
 ************/
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisTemplate redisTemplate;

    /**
     * Lock 획득
     */
    public boolean acquireLock(String lockKey, String requestId, long expireTime) {
        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.MILLISECONDS);
        return isSet != null && isSet;
    }

    /**
     * Lock 해제
     */
    public void releaseLock(String lockKey, String requestId) {
        String storedRequestId = (String) redisTemplate.opsForValue().get(lockKey);
        if (requestId.equals(storedRequestId)) {
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * Lock 획득 실패시 - 재시도
     *
     * @return
     */
    public boolean acquireLockRetry(String lockKey, String requestId, long expireTime) {
        int retryCount = 0;
        int maxRetryCount = 10;
        boolean isLock = false;

        while(!isLock && retryCount < maxRetryCount){
            isLock = this.acquireLock(lockKey, requestId, expireTime);

            if(!isLock) {
                try {
                    Thread.sleep(500);
                    System.out.println("재시도!!!!!!!!!!!!! " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            retryCount ++;
        }

        return !isLock;
    }

}
