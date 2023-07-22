package com.boot.redis.config.aop;

import com.boot.redis.about_redis.lock.RedisLockService;
import com.boot.redis.config.annotation.DistributeLock;
import com.boot.redis.config.redis_config.CustomCommands;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.netty.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {

//    private final RedisTemplate template;
    private final RedisLockService service;


    @Around("@annotation(com.boot.redis.config.annotation.DistributeLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock annotation = method.getAnnotation(DistributeLock.class);
//        service.acquireLock()

        return null;
    }


    /**
     * Lua 스크립트를 통한 LOCK Check(get/set)
     * @date: 2023-07-21 18:06 기준 수정중.
     *
     * - param + Obj 부분 수정 해야함.
     */
    private void updateResponseMsgToSSO(String message, String responseMessage) {
//        try {
//            ObjectMapper ob = new ObjectMapper();
//            RedisCommandFactory factory = new RedisCommandFactory((StatefulConnection<?, ?>) template.getConnectionFactory());
//            CustomCommands commands = factory.getCommands(CustomCommands.class); // Lua Script

            // FIXME Logic Start ...
//            Object obj = commands.fcall_responseJob(
//                    Constant.REDIS_RESPONSE_JOB_FUNCTION_NAME.getBytes(StandardCharsets.UTF_8),
//                    Constant.REDIS_RESPONSE_JOB_FUNCTION_KEY_COUNT,
//                    (message.getAgent() + Constant.AGENTS_JOBS_POSTFIX).getBytes(StandardCharsets.UTF_8),
//                    (message.getAgent() + Constant.AGENTS_INBOX_POSTFIX).getBytes(StandardCharsets.UTF_8),
//                    message.getReferenceNumber().getBytes(StandardCharsets.UTF_8),
//                    message.getTyp().getBytes(StandardCharsets.UTF_8),
//                    ob.writeValueAsBytes(responseMessage));
//        }catch (Exception e){
//            log.error("Redis Lua Script error : {}", e.getMessage());
//            e.printStackTrace();
//        }
    }

}
