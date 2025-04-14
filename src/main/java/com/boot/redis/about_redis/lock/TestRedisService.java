package com.boot.redis.about_redis.lock;

import com.boot.redis.about_sync.domain.SyncObject;
import com.boot.redis.about_sync.model.SyncJpaRepository;
import com.boot.redis.config.annotation.DistributeLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/************
 * @info : Redis Lock & @Transactional Test Service
 * @name : RedisLockService
 * @date : 2023/07/14 10:55 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Redis Lock 호출 테스트를 위한 service Class
 ************/
@Service
@RequiredArgsConstructor
@Slf4j
public class TestRedisService {

    private final RedisLockService redisLockService;
    private final SyncJpaRepository syncJpaRepository;

    @Transactional
    public void lockTask() {
        String lockKey = "theLock";
        String requestId = UUID.randomUUID().toString();
        long expireTime = 5000L;

        boolean isLock = redisLockService.acquireLock(lockKey, requestId, expireTime);
        if(isLock){
            // Lock 획득
            try {
                // TODO : Lock 획득 후 처리할 비즈니스 로직?
            } finally {
                // Lock 해제
                redisLockService.releaseLock(lockKey, requestId);
            }
        }else {
            log.warn("Redis Lock 획득 실패!");
        }//else
    }


    /**
     * @package : com.boot.redis.about_redis.lock
     * @name : TestRedisService.java
     * @date : 2025. 4. 15. 오전 12:09
     * @author : lucaskang(swings134man)
     * @Description: Annotation 으로 Distribute Lock 처리 Test Methods
     * - syncName = SyncObject 의 Name
    **/
    @DistributeLock(lockKey = "#lockName", retryCount = 100, expireTime = 7000L)
    @Transactional
    public void decreaseSyncValue(String lockName, String syncName) {
        SyncObject syncObject = syncJpaRepository.findByName(syncName).orElseThrow(IllegalArgumentException::new);

        syncObject.decrease();
    }

}
