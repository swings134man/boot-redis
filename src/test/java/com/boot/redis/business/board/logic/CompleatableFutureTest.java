package com.boot.redis.business.board.logic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class CompleatableFutureTest {

//    @Test
//    void threadTest()  {
//        test2();
//    }


    // 여러건의 repo 조회 시 병렬로 조회
    @DisplayName("CompleatableFuture Async Test")
    @Test
    void futureManyAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> cf1 = CompletableFuture.supplyAsync(() -> {
            List<String> list = List.of("1", "2", "3", "4", "5");
            return list;
        });

        CompletableFuture<Object> cf2 = CompletableFuture.supplyAsync(() -> {
            // 해당 부분에 DB 조회 로직 작성
            List<String> list = List.of("6", "7", "8", "9", "10");
            return list;
        });


        CompletableFuture.allOf(cf1).join();

        Map<String, Object> map = new HashMap<>();

        map.put("result1", cf1.get());
        map.put("result2", cf2.get());


        System.out.println("map = " + map.get("result1"));
        System.out.println("map2 = " + map.get("result2"));
        Assertions.assertThat(map.get("result1")).isNotNull();
        Assertions.assertThat(map.get("result2")).isNotNull();
    }
}