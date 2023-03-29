package com.boot.redis.seconds.business;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/************
 * @info : Redis - DAO : RedisTemplate 사용 클래스
 * @name : SeDAO
 * @date : 2023/03/30 2:12 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Repository
@RequiredArgsConstructor
@Slf4j
public class SeDAO {

    private final RedisTemplate<String, String> template;

}
