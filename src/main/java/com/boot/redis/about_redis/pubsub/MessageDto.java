package com.boot.redis.about_redis.pubsub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String message;
    public String sender;
    public String roomId;
}
