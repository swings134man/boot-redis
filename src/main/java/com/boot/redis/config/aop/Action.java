package com.boot.redis.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
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

            loggingRequestBody();
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

    private static void loggingRequestBody() {
        HttpServletRequest request = request();

        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;

        String body = new String(requestWrapper.getContentAsByteArray());
        log.info("Request Body : {}", body);
    }

    private static HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
