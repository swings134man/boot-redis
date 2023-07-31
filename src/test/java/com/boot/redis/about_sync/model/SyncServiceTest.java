package com.boot.redis.about_sync.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/************
 * @info : @Synchronized 테스트를 위한 테스트 클래스
 * @name : SyncServiceTest
 * @date : 2023/07/31 9:39 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : TODO: @Transactional 사용 테스트 필요 ->
 *
 * Transactional = 프록시 방식의 AOP
 *  1. 트랜잭션 프록시 -> 트랜잭션 처리 로직 시작 -> 트랜잭션 시작 후 -> 실제 서비스 호출 -> 트랜잭션 종료.
 *  2. 트랜잭션 종료 되기 전에 다른  Thread 가 Synchronized 메서드에 접근하면, 트랜잭션 종료 전에 다른 Thread 가 접근 가능.
 *
 *  결론: Synchronized 를 Transactional 보다 먼저 호출
 *  -> Synchronized 를 먼저 호출하면, 트랜잭션 종료 전에 다른 Thread 가 접근 불가능.
 ************/
@SpringBootTest
class SyncServiceTest {

    private static Long count = 0L;

    @AfterEach
    void after() {count = 0L;}

    void increase(){
        count += 1L;
    }

    synchronized void sync_increase(){
        count += 1L;
    }


    // Fail -> Race Condition 발생
    // 여러개의 Thread 동시 접근시 count 값이 100이 아닌 99, 98 등의 값이 나옴
    // 이유는 increase() 메서드가 Thread Safe 하지 않기 때문
    @Test
    void non_sync() {
        ExecutorService service = Executors.newCachedThreadPool(); // Thread 를 동적으로 생성.
        CountDownLatch cdl = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            service.submit(() -> {
                try {
                    increase();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        Assertions.assertThat(count).isEqualTo(100);
    }// non_sync()


    // PASS

    /**
     * synchronized 키워드를 사용하여 Thread Safe 하게 만듬
     * 결론 : synchronized 키워드를 사용하여 Thread Safe 하게 만들 수 있음
     */
    @Test
    void sync_test() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch cdl = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            service.submit(() -> {
                try {
                    sync_increase();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        Assertions.assertThat(count).isEqualTo(100);
    }// sync_test()
}