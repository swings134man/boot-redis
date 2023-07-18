package com.boot.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Bean(name = "asyncThread")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 실행 대기 Thread 수
        executor.setMaxPoolSize(10); // 동시 동작 최대 Thread 수
        executor.setQueueCapacity(10); // MaxPoolSize 초과 시 대기 Queue 수
        executor.setThreadNamePrefix("async-thread-"); // Thread 이름 prefix
        executor.initialize();
        return executor;
    }

}
