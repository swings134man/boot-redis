package com.boot.redis.first.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RedisUser {

    @Id
    private String id; // userId: 입력안하면 임의의 값 생성됨.

    private String name;
    private String major; // 전공: back, front
    private int age;

//    private List<String> skils;


}
