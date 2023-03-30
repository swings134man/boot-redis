package com.boot.redis.first.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
class RedisUserSerivceTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // String 구조
    @Test
    void testStrings() {
        //given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "first";

        //when
        valueOperations.set(key, "helloWorld!");

        //then
        String value = valueOperations.get(key);
        assertThat(value).isEqualTo("helloWorld!");
    }

    // Set 구조
    // redis-cli 조회시 smembers {key} 로 조회해야함.
    @Test
    void testSet() {
        //given
        SetOperations<String, String> valueSet = redisTemplate.opsForSet();
        String key = "forset";

        //when
        valueSet.add(key, "a", "b", "c", "d", "c"); // c 중복! -> 하나만 나와야함.

        //then
        Set<String> members = valueSet.members(key); // set 타입은 members로 관리됨.
        Long size = valueSet.size(key);// 해당 set{key}의 size

        assertThat(members).containsOnly("a", "b", "c", "d");
        assertThat(size).isEqualTo(4);
    }

    // Hash 구조
    @Test
    void testHash() {
        //given
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String key = "hashKey";

        //when
        hashOperations.put(key, "data1", "this is value");


        //then
        Object value = hashOperations.get(key, "data1");
        assertThat(value).isEqualTo("this is value");
        System.out.println("value = " + value);

        // Map Entries
        Map<Object, Object> entries = hashOperations.entries(key);
        assertThat(entries.keySet()).containsExactly("data1"); // Key 포함
        assertThat(entries.values()).containsExactly("this is value"); // value 포함 여부
        System.out.println("entries = " + entries);

        Long size = hashOperations.size(key);
        assertThat(size).isEqualTo(entries.size());
        System.out.println("size = " + size);
    }

}