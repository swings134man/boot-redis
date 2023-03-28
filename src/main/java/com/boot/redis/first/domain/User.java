package com.boot.redis.first.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
@Getter
@Setter
@ToString
//@AllArgsConstructor
//@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;
    private int age;

    @Builder
    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User() {}
}
