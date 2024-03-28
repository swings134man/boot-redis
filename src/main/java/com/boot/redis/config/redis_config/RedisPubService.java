package com.boot.redis.config.redis_config;

import com.boot.redis.about_redis.pubsub.MessageDto;
import com.boot.redis.about_redis.pubsub.channels.TestRoomListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubService {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher;

    // 각 Channel 별 Listener
    private final RedisSubscribeListener redisSubscribeListener;
//    private final TestRoomListener testRoomListener;


    /**
     * Channel 별 Message 전송
     * @param
     */
    public void pubMsgChannel(String channel ,MessageDto message) {
        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, new ChannelTopic(channel));

        //2. Message 전송
        redisPublisher.publish(new ChannelTopic(channel), message);
    }


    /**
     * Channel TEST
     * @param
     */
//    public void subTestRoom(MessageDto message) {
//        redisMessageListenerContainer.addMessageListener(testRoomListener, new ChannelTopic("testroom"));
//
//        //2. Message 전송
//        redisPublisher.publish(new ChannelTopic("testroom"), message);
//    }


}
