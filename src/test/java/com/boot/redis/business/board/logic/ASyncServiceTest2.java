package com.boot.redis.business.board.logic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;


// Async Test
@SpringBootTest
class ASyncServiceTest2 {

    @Test
    void asyncOne() {

        // 1. 비동기 작업 생성, 실행
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Thread Start : " + Thread.currentThread().getName());
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            //return
            return "Async Task Stop";
        });

        // 작업 완료시 결과 처리
        cf.thenAccept(res -> System.out.println("작업 완료 return = " + res));

        // Main Thread 가 종료되지 않도록 대기
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }// asyncOne

}