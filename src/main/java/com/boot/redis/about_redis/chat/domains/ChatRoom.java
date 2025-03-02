package com.boot.redis.about_redis.chat.domains;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 111111L;

    private String roomId;
    private String roomName;

    public static ChatRoom createRoom(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = name;
        return chatRoom;
    }
}
