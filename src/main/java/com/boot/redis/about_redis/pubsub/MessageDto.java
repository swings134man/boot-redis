package com.boot.redis.about_redis.pubsub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message; // 전송할 메세지 내용
    private String sender; // 메세지 발신자
    private String roomId; // 메세지 방 번호 || 타겟 Channel
    private String type; // 메세지 타입(msg, enter{신규 접속})
}
