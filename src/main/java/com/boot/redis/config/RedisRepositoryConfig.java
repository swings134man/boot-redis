package com.boot.redis.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/************
 * @info : Redis Repository Config 클래스
 * @name : RedisRepositoryConfig
 * @date : 2023/03/29 2:32 AM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description : Lettuce 사용(비동기 요청 처리), RedisRepository 방식.
 ************/
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@EnableCaching
public class RedisRepositoryConfig {

    private final RedisProperties redisProperties;


    // lettuce
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // redisTemplate: default Value Type = JSON
    // If you want to use String Type, you can change ValueSerializer to StringRedisSerializer or Use StringRedisTemplate
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());   //connection
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key
//        redisTemplate.setValueSerializer(new StringRedisSerializer());  // value
//        redisTemplate.setKeySerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Java Obj <-> JSON -> String KEY
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Java Obj <-> JSON -> String Value
        return redisTemplate;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }

    // Redis Cache
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
//                .prefixKeysWith("Test:") // Key Prefix로 "Test:"를 앞에 붙여 저장
                .computePrefixWith(CacheKeyPrefix.prefixed("Cache:")) // Prefix
                .disableCachingNullValues() // Null Value Cache 안함.
                .entryTtl(Duration.ofMinutes(1)); // 캐시 수명 1분
        builder.cacheDefaults(configuration);
        return builder.build();
    }

    @Bean
    public CacheErrorHandler errorHandle() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.error("Redis Cache GET Error --key:" + key + " Exception information:" + exception);
                // if Get Error -> Delete Cache Data
                // This Code makes Cache Data to be reloaded.
                // !!But Many Times need to reload!! -> So, change this code
//                redisTemplate.delete(cache.getName() + "*");
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.error("Redis Cache PUT Error --key:" + key + " Exception information:" + exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.error("Redis Cache EVICT Error --key:" + key + " Exception information:" + exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.error("Redis Cache CLEAR Error -- Exception information:" + exception);
            }
        };
        return cacheErrorHandler;
    }

    /**
     * Redis pub/sub 메시지 처리 Listener
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

    /**
     * Redis pub/sub 메시지 처리 Topic
     */
    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("chatroom");
    }
}
