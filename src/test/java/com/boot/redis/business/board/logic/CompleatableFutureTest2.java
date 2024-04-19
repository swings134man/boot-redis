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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @info : CompleatableFuture Test
 * @name : CompleatableFutureTest2
 * @date : 2023/04/01 5:46 PM
 * @version : 1.0.0
 * @Description : Test For Use Stream Task List With CompleatableFuture
 *
 * Also This Test includes Type Assertion Test
 *
 */
@SpringBootTest
class CompleatableFutureTest2 {


    // 여러건의 repo 조회 시 병렬로 조회
    @DisplayName("CompleatableFuture Stream Task Test")
    @Test
    void streamTaskTest() throws ExecutionException, InterruptedException {

        Stream<Supplier<?>> tasks = Stream.of(
                () -> {
                    List<String> list = List.of("1", "2", "3", "4", "5");
                    return list;
                },
                () -> {
                    // 해당 부분에 DB 조회 로직 작성
                    // ex) List<?> list = service.getDBData(); return list;
                    List<String> list = List.of("6", "7", "8", "9", "10");
                    return list;
                }
        );

        List<?> streamList = tasks
                .map(CompletableFuture::supplyAsync).collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join).collect(Collectors.toList());

        int idx = 0;
        System.out.println("idx 0 = " + streamList.get(idx++));
        System.out.println("idx 1 = " + streamList.get(idx++));

        // This Test Success
        // streamList.get(0) Object Is Return List.toString(); So, It's Type is String

        // Success Reason : List.toString() Return String
        // List Interface toString() Method Return String
        Assertions.assertThat(streamList.get(0)).hasToString("[1, 2, 3, 4, 5]");

        //Assertions.assertThat(streamList.get(0)).isInstanceOf(String.class); // false!!!
        Assertions.assertThat(streamList.get(1)).isInstanceOf(List.class);

        // to List
        List<String> list = (List) streamList.get(0);
        Assertions.assertThat(list).isInstanceOf(List.class);
    }// method
}