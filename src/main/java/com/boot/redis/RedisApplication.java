package com.boot.redis;

import com.boot.redis.config.properties.ApiGenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableConfigurationProperties(ApiGenProperties.class)
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
