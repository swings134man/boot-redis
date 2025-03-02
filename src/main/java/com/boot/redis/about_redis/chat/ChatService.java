package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.boot.redis.config.redis_config.RedisPublisher;
import com.boot.redis.config.redis_config.RedisSubscribeListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher; // Publisher
    private final RedisSubscribeListener redisSubscribeListener;

    // enter(subscribe)
    public void enterRoom(String roomId) {
        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, new ChannelTopic(roomId));
    }

    // send
    public void msgSend(MessageDto dto) {
        redisPublisher.publish(new ChannelTopic(dto.getRoomId()), dto);
    }

}
