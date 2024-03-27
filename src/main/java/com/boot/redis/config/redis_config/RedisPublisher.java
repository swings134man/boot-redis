package com.boot.redis.config.redis_config;

import com.boot.redis.about_redis.pubsub.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/************
 * @info : Redis - Publisher Class
 * @name : RedisPublisher
 * @date : 2023/07/16 5:02 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    public RedisPublisher(RedisTemplate<String, Object> template) {
        this.template = template;
    }


    /**
     * 문자열 publish
     * TODO: Redis Config 에서 setValueSerializer (String) 설정시 Exception
     */
    public void publish(ChannelTopic topic, MessageDto dto) {
        template.convertAndSend(topic.getTopic(), dto);
    }

    /**
     * Data 형 publish
     */
    public void publish(ChannelTopic topic ,String data) {
        template.convertAndSend(topic.getTopic(), data);
    }



}
