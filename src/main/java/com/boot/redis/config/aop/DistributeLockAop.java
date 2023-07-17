package com.boot.redis.config.aop;

import com.boot.redis.about_redis.lock.RedisLockService;
import com.boot.redis.config.annotation.DistributeLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {

    private final RedisLockService service;


    @Around("@annotation(com.boot.redis.config.annotation.DistributeLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock annotation = method.getAnnotation(DistributeLock.class);

//        service.acquireLock()


        return null;
    }

}
