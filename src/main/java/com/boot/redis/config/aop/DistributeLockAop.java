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
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {

    private final RedisLockService redisLockService;

    @Around("@annotation(com.boot.redis.config.annotation.DistributeLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 0. SpEL
        EvaluationContext context = new StandardEvaluationContext();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        ExpressionParser parser = new SpelExpressionParser();

        // 1. For Lock Values
        DistributeLock annotation = method.getAnnotation(DistributeLock.class);
        String lockKey = parser.parseExpression(method.getAnnotation(DistributeLock.class).lockKey()).getValue(context, String.class);
        String requestId = UUID.randomUUID().toString();
        int retryCount = annotation.retryCount();
        long expireTime = annotation.expireTime(); // ms(Default 5sec)

        // 2. Get Lock
        boolean isLock = redisLockService.acquireLockRetry(lockKey, requestId, retryCount, expireTime);

        if(!isLock) {
            return false;
        }

        // 3. Proceed
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Error occurred while executing method: {}", method.getName(), e);
            throw e;
        } finally {
            // 4. Release Lock
            redisLockService.releaseLock(lockKey, requestId);
        }
    }


}
