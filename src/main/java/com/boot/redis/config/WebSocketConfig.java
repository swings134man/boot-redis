package com.boot.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket // webSocket
@EnableWebSocketMessageBroker // STOMP
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * @info : STOMP 설정
     * @param registry
     * @description : heartBeat 의 경우 Client 에서 client.heartbeat.outgoing, client.heartbeat.incoming 설정 필요.
     *
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); //msg 받을경로(구독)
//                .setTaskScheduler(heartbeatScheduler())
//                .setHeartbeatValue(new long[]{300000, 300000}); // heartBeat 5min(300s)
        registry.setApplicationDestinationPrefixes("/pub"); //msg 보낼경로(발행)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*");
    }


    // Heartbeat scheduler
//    @Bean
//    public ThreadPoolTaskScheduler heartbeatScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(1);
//        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
//        scheduler.initialize();
//        return scheduler;
//    }
}
