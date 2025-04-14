package com.boot.redis.about_redis.lock;

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
 *
 * -- Lettuce Spin Lock 방식
 * - 1개의 Redis Node 의 단일책임 방식 -> 대규모 방식(N개 Node) 채용시 RedLock 사용 권장
 *
 * - 단일 node Redis 일 경우, 장애시 문제발생 + redis 성능 문제 발생(부하)
 ************/
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisTemplate redisTemplate;

    private static final String LOCK_PREFIX = "LOCK:";
    private static final long SPIN_INTERVAL_MIN = 100; // 최소 재시도 간격 (100ms)
    private static final long SPIN_INTERVAL_MAX = 500; // 최대 재시도 간격 (500ms)

    /**
     * Lock 획득
     */
    public boolean acquireLock(String lockKey, String requestId, long expireTime) {
        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(getLockKey(lockKey), requestId, expireTime, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(isSet); // 성공시 true, 실패시 false
    }

    /**
     * Lock 해제
     */
    public void releaseLock(String lockKey, String requestId) {
        String storedRequestId = (String) redisTemplate.opsForValue().get(getLockKey(lockKey));
        if (requestId.equals(storedRequestId)) {
            redisTemplate.delete(getLockKey(lockKey));
        }
    }

    /**
     * Lock 획득 실패시 - 재시도
     * - Random 한 재시도 시간을 두고 Lock 획득 시도
     *   최대 20회 재시도 후 Lock 획득 실패시 false 반환
     * @param requestId = UUID.randomUUID().toString()
     * @param expireTime = Lock Expire Time(ms, Millisecond)
     * @return boolean = false
     */
    public boolean acquireLockRetry(String lockKey, String requestId, long expireTime) {
        int retryCount = 0;
        int maxRetryCount = 20;
        boolean isLock = false;

        while(!isLock && retryCount < maxRetryCount){
            isLock = this.acquireLock(lockKey, requestId, expireTime);

            if(!isLock) {
                try {
                    Thread.sleep(getRandomSleepTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            retryCount ++;
        }
        return isLock;
    }

    /**
     * @package : com.boot.redis.about_redis.lock
     * @name : RedisLockService.java
     * @date : 2025. 4. 15. 오전 12:39
     * @author : lucaskang(swings134man)
     * @Description: 재시도 횟수를 지정하여 Lock 획득 시도
    **/
    public boolean acquireLockRetry(String lockKey, String requestId, int retryCount, long expireTime) {
        int count = 0;
        boolean isLock = false;

        while(!isLock && count < retryCount){
            isLock = this.acquireLock(lockKey, requestId, expireTime);

            if(!isLock) {
                try {
                    Thread.sleep(getRandomSleepTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            count ++;
        }
        return isLock;
    }


    /**
     * 랜덤한 재시도 시간 반환 (100ms ~ 500ms) - BackOff Time(Busy Waiting)
     */
    private long getRandomSleepTime() {
        return SPIN_INTERVAL_MIN + (long) (Math.random() * (SPIN_INTERVAL_MAX - SPIN_INTERVAL_MIN));
    }

    private String getLockKey(String key) {
        return LOCK_PREFIX + key;
    }

}
