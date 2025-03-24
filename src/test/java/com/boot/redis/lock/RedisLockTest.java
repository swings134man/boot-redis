package com.boot.redis.lock;

import com.boot.redis.about_redis.lock.RedisLockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @package : com.boot.redis.lock
 * @name : RedisLockTest.java
 * @date : 2025. 3. 21. 오후 11:35
 * @author : lucaskang(swings134man)
 * @Description: Redis Lock Test v2
 * - 재시도를 제외한, 획득, 실패 , TTL 만료 후 테스트, 여러 Thread 동시 Lock 획득 테스트
**/
@SpringBootTest
public class RedisLockTest {

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    @DisplayName("Lock - 획득, 실패")
    public void lock_획득_실패_테스트() {
        // given
        String lockKey = "test-lock";
        String requestId = UUID.randomUUID().toString();
        long expireTime = 3000L; // 3s

        // when
        boolean isLock = redisLockService.acquireLock(lockKey, requestId, expireTime);

        // then
        assertThat(isLock).isTrue();
        assertThat(redisTemplate.opsForValue().get("LOCK:" + lockKey)).isEqualTo(requestId);

        // when Lock 해제
        redisLockService.releaseLock(lockKey, requestId);

        // then Lock 해제 확인
        assertThat(redisTemplate.opsForValue().get("LOCK:" + lockKey)).isNull();
    }

    @Test
    @DisplayName("Lock - 동일한 LockKey에 중복 획득 불가")
    void 동일한_LockKey에_중복_획득_불가_테스트() {
        // Given
        String lockKey = "duplicate-lock";
        String requestId1 = UUID.randomUUID().toString();
        String requestId2 = UUID.randomUUID().toString();
        long expireTime = 5000;

        // When: 첫 번째 요청이 Lock 획득
        boolean firstLock = redisLockService.acquireLock(lockKey, requestId1, expireTime);

        // Then: 첫 번째 요청은 Lock을 획득해야 함
        assertThat(firstLock).isTrue();

        // When: 두 번째 요청이 동일한 LockKey로 Lock 시도
        boolean secondLock = redisLockService.acquireLock(lockKey, requestId2, expireTime);

        // Then: 두 번째 요청은 Lock 획득 실패해야 함
        assertThat(secondLock).isFalse();
    }

    @Test
    @DisplayName("Lock - TTL 만료 후 테스트")
    void Lock_TTL_만료후_다른_프로세스가_Lock_획득_가능한지_테스트() throws InterruptedException {
        // Given
        String lockKey = "ttl-lock";
        String requestId1 = UUID.randomUUID().toString();
        String requestId2 = UUID.randomUUID().toString();
        long expireTime = 2000; // 2초 후 만료

        // When: 첫 번째 요청이 Lock 획득
        boolean firstLock = redisLockService.acquireLock(lockKey, requestId1, expireTime);

        // Then: 첫 번째 요청이 Lock을 획득해야 함
        assertThat(firstLock).isTrue();

        // 2초 후 TTL 만료 확인
        Thread.sleep(2500);
        assertThat(redisTemplate.opsForValue().get("LOCK:" + lockKey)).isNull();

        // When: 두 번째 요청이 Lock 획득 시도
        boolean secondLock = redisLockService.acquireLock(lockKey, requestId2, expireTime);

        // Then: 두 번째 요청이 Lock을 획득해야 함
        assertThat(secondLock).isTrue();
    }

    @Test
    @DisplayName("Lock - 여러 Thread 동시 Lock 획득 테스트")
    void 여러_스레드가_동시에_Lock을_획득하려고_할때_하나만_획득해야_함() throws InterruptedException {
        // Given
        String lockKey = "concurrent-lock";
        long expireTime = 5000; // 5초

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        String[] successfulRequestId = new String[1];

        // When: 여러 개의 스레드가 동시에 Lock을 획득 시도
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                String requestId = UUID.randomUUID().toString();
                boolean acquired = redisLockService.acquireLock(lockKey, requestId, expireTime);

                if (acquired) {
                    successfulRequestId[0] = requestId;
                }

                latch.countDown();
            });
        }

        latch.await(); // 모든 스레드 대기

        // Then: 하나의 스레드만 Lock을 획득해야 함
        assertThat(successfulRequestId[0]).isNotNull();
        assertThat(redisTemplate.opsForValue().get("LOCK:" + lockKey)).isEqualTo(successfulRequestId[0]);
    }

    @Test
    @DisplayName("Retry - 재시도 획득 테스트 (일정 수량 획득)")
    void retry_재시도_획득_테스트() throws InterruptedException {
        // given
        String lockKey = "test-lock";
        long expireTime = 5000L; // 5s

        int threadCount = 15;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger targetCount = new AtomicInteger(0); // 수량

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                String requestId = UUID.randomUUID().toString();
                boolean isLock = redisLockService.acquireLockRetry(lockKey, requestId, expireTime);

                if(isLock) {
                    targetCount.incrementAndGet();
                    System.out.println("Lock 획득 성공: " + targetCount.get());
                    redisLockService.releaseLock(lockKey, requestId);
                }

                latch.countDown();
            });
        }

        latch.await();

        // then
        assertThat(targetCount.get()).isEqualTo(15);
    }

    @Test
    @DisplayName("Retry : 한정 수량 만큼만 되는지 확인")
    void retry_한정수량() throws InterruptedException {
        // given
        String lockKey = "test-lock";
        long expireTime = 5000L; // 5s

        int threadCount = 15;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger targetCount = new AtomicInteger(15); // 수량

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                String requestId = UUID.randomUUID().toString();
                boolean isLock = redisLockService.acquireLockRetry(lockKey, requestId, expireTime);

                if (isLock) {
                    try {
                        if (targetCount.get() == 0) {
                            System.err.println("Count Error");
                        } else {
                            targetCount.decrementAndGet();
                            System.out.println("Lock 획득 성공: " + targetCount.get());
                        }
                    } finally {
                        redisLockService.releaseLock(lockKey, requestId);
                    }
                }

                latch.countDown();
            });
        }

        latch.await();

        // then
        assertThat(targetCount.get()).isEqualTo(0);
    }
}
