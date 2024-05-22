package com.boot.redis.about_redis.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class TestSample {

//    @Autowired TestService testService;

    @DisplayName("1. Service Test")
    @Test
    @Rollback
    void test() {
        // 해당 메서드는 컨트롤러라고 생각~
//        testService.test();

        // Return 값이 있을 경우?
//        Test test = testService.test();
//        System.out.println("test = " + test);

        // test() 메서드 실행 이후 테스트 종료 되면 자동 RollBack 처리!
        // -> 꼭 DB 확인해볼것~


    }
}
