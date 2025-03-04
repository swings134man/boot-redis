package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.pubsub.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void send(MessageDto message) {
        //FIXME: Browser 에서 subscribe 처리 해야함.
//        if(message.getType().equals("enter")){
//            chatService.enterRoom(message.getRoomId());
//            message.setMessage(message.getSender() + " JOIN");
//        }
        chatService.msgSend(message);
    }
}
