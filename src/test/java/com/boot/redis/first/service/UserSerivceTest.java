package com.boot.redis.first.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

/************
 * @info : Redis - Service Test - Template
 * @name : UserSerivceTest
 * @date : 2023/03/29 6:45 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@SpringBootTest
class UserSerivceTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testStrings() {
        //given
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String key = "first";

        //when
        valueOperations.set(key, "helloWorld!");

        //then
        String value = (String) valueOperations.get(key);
        Assertions.assertThat(value).isEqualTo("helloWorld!");
    }

}