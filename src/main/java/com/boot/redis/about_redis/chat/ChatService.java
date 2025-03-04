package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.boot.redis.config.redis_config.RedisPublisher;
import com.boot.redis.config.redis_config.RedisSubMsgListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher; // Publisher
    private final RedisSubMsgListener redisSubscribeListener;

    private Map<String, ChannelTopic> topics = new ConcurrentHashMap<>(); // Topic 객체 list

    @PostConstruct
    public void initTopicsFromRedis() {
        Map<Object, Object> chatRooms = redisTemplate.opsForHash().entries("CHAT_ROOMS");

        for (Object key : chatRooms.keySet()) {
            String roomId = (String) key;
            ChannelTopic topic = new ChannelTopic(roomId);
            topics.put(roomId, topic);
            redisMessageListenerContainer.addMessageListener(redisSubscribeListener, topic);
            log.info("🔄 Redis에서 기존 채팅방 복구: {}", roomId);
        }
    }


    // generate New Room(In-Memory)
    public void genNewRoom(String roomId) {
        ChannelTopic channelTopic = topics.get(roomId);
        if(channelTopic == null) {
            channelTopic = new ChannelTopic(roomId);
            redisMessageListenerContainer.addMessageListener(redisSubscribeListener, channelTopic);
            topics.put(roomId, channelTopic);
        }
    }

    // 특정 채팅방 삭제
    public void deleteRoom(String roomId) {
        ChannelTopic channelTopic = topics.get(roomId);
        if(channelTopic != null) {
            // 1. Clients 에게 Delete FLAG 전송으로 DisConnection 하게 함.(Front 제어 필요)
            MessageDto messageDto = new MessageDto();
            messageDto.setRoomId(roomId);
            messageDto.setType("DELETE");
            msgSend(messageDto);

            // 2. Redis Listener 삭제
            redisMessageListenerContainer.removeMessageListener(redisSubscribeListener, channelTopic);

            // 3. Topic 삭제
            topics.remove(roomId);
            log.info("Chat Room Deleted: {}", roomId);
        }
    }

    // exit(unsubscribe) : TODO: Params 에 sessionId 추가(enterRoom 도 마찬가지) STOMP SessionId
    public void leaveRoom(String roomId, String sessionId) {
    }

    // send
    public void msgSend(MessageDto dto) {
        if(topics.get(dto.getRoomId()) != null) {
            redisPublisher.publish(topics.get(dto.getRoomId()), dto);
        }else {
            log.error("채팅방이 존재하지 않습니다.");
        }
    }
}
