package com.boot.redis.lock;

import com.boot.redis.about_redis.lock.TestRedisService;
import com.boot.redis.about_sync.domain.SyncObject;
import com.boot.redis.about_sync.model.SyncJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class DistributedLockTest {

    @Autowired
    private TestRedisService redisService;

    @Autowired
    private SyncJpaRepository syncJpaRepository;

    private SyncObject syncObject;

    @BeforeEach
    void setUp() {
        syncJpaRepository.deleteAll();

        // Init DB Row Data
        syncObject = new SyncObject();
        syncObject.setName("lock_one");
        syncObject.setValue(100);

        syncJpaRepository.save(syncObject);
    }



    @Test
    @DisplayName("1. Distributed Lock Test: Annotation")
    void 특정값차감_분산락_동시성_100개() throws InterruptedException {
        int numberOfThreads = 100; // 동시성 테스트를 위한 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출 (락의 key는 Object의 name으로 설정)
                    redisService.decreaseSyncValue(syncObject.getName(), syncObject.getName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 최종 확인
        SyncObject result = syncJpaRepository.findByName(syncObject.getName())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(result.getValue()).isZero();
        System.out.println("Final Value: " + result.getValue());
    }


}
