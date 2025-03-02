package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.boot.redis.config.redis_config.RedisPublisher;
import com.boot.redis.config.redis_config.RedisSubscribeListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher; // Publisher
    private final RedisSubscribeListener redisSubscribeListener;

    // User 가 구독한 채널(채팅방)
    private final Map<String, String> userSubscriptions = new ConcurrentHashMap<>();

    // enter(subscribe)
    public void enterRoom(String roomId) {
        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, new ChannelTopic(roomId));
    }

    // exit(unsubscribe) : TODO: Params 에 sessionId 추가(enterRoom 도 마찬가지) STOMP SessionId
    public void leaveRoom(String roomId, String sessionId) {
        if(!userSubscriptions.containsKey(sessionId)) return;

        log.info("User[{}] leave Room[{}]", sessionId, roomId);
//        redisMessageListenerContainer.removeMessageListener(redisSubscribeListener, new ChannelTopic(roomId));// 문제있는코드

        userSubscriptions.remove(sessionId);
    }

    // send
    public void msgSend(MessageDto dto) {
        redisPublisher.publish(new ChannelTopic(dto.getRoomId()), dto);
    }

}
