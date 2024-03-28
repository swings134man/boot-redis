package com.boot.redis.config.redis_config;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/************
 * @info : Redis Subscribe Class
 * @name : RedisSubscribe
 * @date : 2023/07/16 5:09 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Service
@Slf4j
public class RedisSubscribe implements MessageListener {

    private final RedisTemplate<String, Object> template;
    private final ObjectMapper objectMapper;

    public RedisSubscribe(RedisTemplate<String, Object> template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 발행된 데이터를 Deserialize
            log.info("Redis Subscriber 호출");
            String publishMessage = template
                    .getStringSerializer().deserialize(message.getBody());
            log.info("Publish Message : " + publishMessage);

            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            log.info("Redis Subscribe Message : " + messageDto.getMessage());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
