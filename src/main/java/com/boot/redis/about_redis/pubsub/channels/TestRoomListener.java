package com.boot.redis.about_redis.pubsub.channels;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Redis Subscribe Test Class
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestRoomListener implements MessageListener {

    private final RedisTemplate<String, Object> template;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 발행된 데이터를 Deserialize
            String publishMessage = template
                    .getStringSerializer().deserialize(message.getBody());

            // Deserialize된 데이터를 MessageDto로 변환
            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            log.info("Redis Subscribe Message : " + messageDto.getMessage());

            // Return || Another Method Call(etc.save to DB)
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
