package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.chat.domains.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @package : com.boot.redis.about_redis.chat.domains
 * @name : ChatRoomService.java
 * @date : 2025. 3. 1. 오후 6:55
 * @author : lucaskang(swings134man)
 * @Description: 채팅방 관리 Service
**/
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CHAT_ROOMS_KEY = "CHAT_ROOMS"; // Redis Hash Key

    // 채팅방 생성
    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.createRoom(name);
        redisTemplate.opsForHash().put(CHAT_ROOMS_KEY, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    // 채팅방 리스트 조회
    public List<ChatRoom> findAllRooms() {
        return redisTemplate.opsForHash().values(CHAT_ROOMS_KEY)
                .stream()
                .map(obj -> (ChatRoom) obj)
                .collect(Collectors.toList());
    }

    // 특정 채팅방 찾기
    public ChatRoom findRoomById(String roomId) {
        return (ChatRoom) redisTemplate.opsForHash().get(CHAT_ROOMS_KEY, roomId);
    }
}
