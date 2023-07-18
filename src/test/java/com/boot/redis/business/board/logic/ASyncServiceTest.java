package com.boot.redis.business.board.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class ASyncServiceTest {

    @Test
    void threadTest()  {
        test2();
    }


    @DisplayName("Async Only Test")
    @Test
    @Async("asyncThread")
    void asyncInsertTest() {
        System.out.println("Thread TEST Name = " + Thread.currentThread().getName());
        String name = "OK";

        // 1. 로직. return 이 없는.
        try {
            Thread.sleep(10000);
            System.out.println("DB Conncetion Test END");

            CompletableFuture<String> res = CompletableFuture.completedFuture(name);
            System.out.println("res = " + res.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }// asyncInsertTest() END


    @DisplayName("컨트롤러 역할")
    @Test
    @Async("asyncThread")
    void test2() {
        System.out.println("Thread MAIN Name = " + Thread.currentThread().getName());

        asyncInsertTest();

        System.out.println("MAIN END");
    }
}