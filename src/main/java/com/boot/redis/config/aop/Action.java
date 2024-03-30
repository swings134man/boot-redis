package com.boot.redis.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@Lazy
public class Action {

    // Annotation Location
    @Pointcut("@annotation(com.boot.redis.config.annotation.ActionMapping)")
    public void enableAction() {}

    // target - All Project Packages
    // 프로메테우스 관련 /actuator 는 제외
    @Pointcut("execution(* com.boot.redis..*.*(..)) && !execution(* com.boot.redis.config..*.*(..))")
    public void cut() {}

    @Before("cut() && enableAction()")
    public void before(JoinPoint joinPoint) {
        // parameter
        Object[] args = joinPoint.getArgs();

        // method
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Action
        if(args.length > 0){
            log.info("Action : {}", method.getName());
            log.info("   -> {} PARAM = {}", method.getName(), Arrays.toString(args));
        }else {
            log.info("Method Name : {}", method.getName());
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void after(JoinPoint joinPoint, Object returnObj) {
        // method
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Action
        log.info(" AFTER RETURNING  -> {} RETURN = {}", method.getName(), returnObj);
    }
}
