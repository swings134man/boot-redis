package com.boot.redis.seconds.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/************
 * @info : Redis - Token Class
 * @name : Tokens
 * @date : 2023/03/30 2:06 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Expire Test
 ************/
@RedisHash(value = "tokens", timeToLive = 20)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tokens {

    @Id
    private String id; // UserId -> 유저는 1개이기 때문.

    private String accessToken;
    private String refreshToken;
}
