package com.boot.redis.about_redis.pubsub;

import com.boot.redis.config.redis_config.RedisPublisher;
import com.boot.redis.config.redis_config.RedisSubscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/************
 * @info : Redis Pub Sub Test Class
 * @name : PubSubClass
 * @date : 2023/07/16 5:12 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/redis/pubsub")
public class PubSubClass {

    private final RedisTemplate<String, Object> template;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher;
    private final RedisSubscribe redisSubscribe;

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageDto message) {
        //1. Redis Message Listener Container를 통해 Subscribe를 시작한다.
        redisMessageListenerContainer.addMessageListener(redisSubscribe, new ChannelTopic("chatroom"));

        //2. Message 전송
        redisPublisher.publish(new ChannelTopic("chatroom"), message);
        log.info("Message Send~ !!!! => {}", message);
    }


    @PostMapping("/send-test")
    public void sendMessageToTest(@RequestBody MessageDto message) {
        //TODO: Channel 별 Topic 생성, Message 전송은 해당 클래스의 방법대로 가능.
        // 다만, 각 Channel 별로 Listener를 생성해야 한다.
        redisMessageListenerContainer.addMessageListener(redisSubscribe, new ChannelTopic("chatroom"));

        //2. Message 전송
        redisPublisher.publish(new ChannelTopic("testroom"), message);
        log.info("Message Send~ !!!! => {}", message);
    }


}
