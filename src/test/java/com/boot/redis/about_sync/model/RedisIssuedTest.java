package com.boot.redis.about_sync.model;

import com.boot.redis.business.board.logic.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
public class RedisIssuedTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testEventLimitCnt() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        IntStream.rangeClosed(1, 200).forEach(i -> {
            executorService.submit(() -> {
                boolean result = boardService.eventLimitCnt((long) i);
                if (!result) {
                    System.out.println("Test finished, limit reached.");
                    System.out.println("User ID Count: " + i);
                    executorService.shutdown();
                }
            });
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

}
