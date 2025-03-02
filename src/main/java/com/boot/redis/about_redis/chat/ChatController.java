package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.chat.domains.ChatRoom;
import com.boot.redis.about_redis.pubsub.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService roomService;

    @PostMapping("/room")
    public ResponseEntity<Object> createRoom(@RequestParam String room) {
        log.info("Redis Generate Room = {}", room);
        roomService.createRoom(room);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getRoomList() {
        List<ChatRoom> allRooms = roomService.findAllRooms();
        return new ResponseEntity<>(allRooms, HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoom> getOneRoom(@PathVariable String roomId) {
        ChatRoom roomById = roomService.findRoomById(roomId);
        return new ResponseEntity<>(roomById, HttpStatus.OK);
    }

    // ----------------------------- Chat -----------------------------
    @PostMapping("/send")
    public ResponseEntity<Object> sendMsg(@RequestBody MessageDto dto) {
        chatService.msgSend(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: Subscribe (Message Listener)
}
