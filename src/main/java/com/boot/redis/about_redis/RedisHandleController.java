package com.boot.redis.about_redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/************
 * @info : Redis Handle Controller
 * @name : RedisHandleController
 * @date : 2023/07/16 6:06 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/redis")
public class RedisHandleController {

    private final RedisTemplate<String, Object> template;

    @GetMapping("/flushAll")
    public String flushAll() {
        template.getConnectionFactory().getConnection().flushAll();
        return "Flush All Success";
    }

}
