package com.boot.redis.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class Action {

    // Annotation Location
    @Pointcut("@annotation(com.boot.redis.config.annotation.ActionMapping)")
    public void enableAction() {}

    // target - All Project Packages
    @Pointcut("execution(* com.boot.redis..*.*(..))")
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
