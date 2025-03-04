package com.boot.redis.config.redis_config;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/************
 * @info : Redis Subscribe Class
 * @name : RedisSubscribe
 * @date : 2023/07/16 5:09 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Redis Subscribe Listener
 *               - Redis에서 발행된 메시지를 수신하는 리스너
 *               - 발행된 메시지를 수신하여 처리 로직으로 전송의 역할까지 수해함.
 *               - 어떤 Channel 에서 발행된 메시지인지 확인하여 처리한다.
 ************/
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubMsgListener implements MessageListener {

    private final RedisTemplate<String, Object> template;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = template
                    .getStringSerializer().deserialize(message.getBody());

            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            // To Clients
            messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
