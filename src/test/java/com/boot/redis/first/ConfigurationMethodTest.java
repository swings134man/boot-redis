package com.boot.redis.first;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
class ConfigurationMethodTest {

    @Autowired
    BeanOne beanOne;

    @Autowired
    TargetBean targetBean;

    @Test
    @DisplayName("1. Configuration 안의 Bean 메서드 호출시 같은 객체 반환")
    void test1() {
        Assertions.assertThat(beanOne.testOne()).isEqualTo(targetBean); // AutoWired 로 주입받은 객체와 같음.

        // new 로 생성한 객체와 비교
        TargetBean test = new TargetBean();
        Assertions.assertThat(beanOne.testOne()).isNotEqualTo(test); // 다른 객체임.
    }
}// test class


@Configuration
class BeanOne {
    // TargetBean 객체를 반환하는 Bean 객체를 생성
    @Bean
    public TargetBean testOne() {
        return new TargetBean();
    }
}

class TargetBean {
    public void one() {
        System.out.println("This is Target Bean");
    }
}