package com.boot.redis.about_redis.chat;

import com.boot.redis.about_redis.chat.domains.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatRoomService roomService;

    @PostMapping()
    public ResponseEntity<Object> createRoom(@RequestParam String room) {
        log.info("Redis Generate Room = {}", room);
        roomService.createRoom(room);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatRoom>> getRoomList() {
        List<ChatRoom> allRooms = roomService.findAllRooms();
        return new ResponseEntity<>(allRooms, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getOneRoom(@PathVariable String roomId) {
        ChatRoom roomById = roomService.findRoomById(roomId);
        return new ResponseEntity<>(roomById, HttpStatus.OK);
    }

    @PostMapping("/delete/{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
