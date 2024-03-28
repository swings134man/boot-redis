package com.boot.redis.about_redis.pubsub;

import com.boot.redis.config.redis_config.RedisPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/************
 * @info : Redis Pub Sub Test Class
 * @name : PubSubClass
 * @date : 2023/07/16 5:12 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Redis Publish 전송 Class(MSG)
 ************/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/redis/pubsub")
public class RedisPubSubController {

    private final RedisPubService redisSubscribeService;


    @PostMapping("/send/")
    public void sendMessage(@RequestParam(required = true) String channel, @RequestBody MessageDto message) {
        log.info("Redis Pub MSG Channel = {}", channel);
        redisSubscribeService.pubMsgChannel(channel, message);
    }



    // FOR TEST ONLY
//    @PostMapping("/send-test")
//    public void sendMessageToTest(@RequestBody MessageDto message) {
//        redisSubscribeService.subTestRoom(message);
//    }


}
