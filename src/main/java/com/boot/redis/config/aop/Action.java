package com.boot.redis.config.aop;

import com.boot.redis.config.util.FieldMap;
import com.boot.redis.config.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
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
    public void before(JoinPoint joinPoint) throws IOException {
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

    private static void loggingRequestBody() throws IOException {
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) extractRequest(ContentCachingRequestWrapper.class, request());

        if(requestWrapper.getContentAsByteArray().length > 0){
            String body = new String(requestWrapper.getContentAsByteArray());
            log.info("Request Body : {}", body);

            // If You Need To Return Map Type Use This
//            FieldMap map = JsonUtil.toMap(body);
//            log.info("@@ filed Map : {}", map);
        }
    }

    private static HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * 이 메서드는 주로 서블릿 필터나 인터셉터에서 원본 HTTP 요청 객체를 처리하거나 검사할 필요가 있을 때 사용됩니다.
     * 예를 들어, 요청의 바디를 로깅하거나, 요청에 포함된 특정 헤더를 검사하는 등의 작업을 수행하기 위해 원본 HttpServletRequest 객체에 접근해야 할 때 유용합니다.
     * HttpServletRequestWrapper는 HttpServletRequest를 확장하거나 변경할 때 사용되는데,
     * 이 메서드는 그러한 확장이나 변경을 거쳐도 원본 요청 객체에 접근할 수 있게 해줍니다.
     *
     * ** ?
     * ServletRequest에서 HttpServletRequest 추출 하여 반환
     * 만약, request가, HttpServletRequestWrapper 인스턴스라면, 재귀적으로 HttpServletRequest 의 실제 구현체를 찾을때 까지 반복, return
     * -> 재귀적으로 HttpServletRequestWrapper가 감싸고 있는 HttpServletRequest를 반환
     * 만약, request 가 HttpServletRequest 또는 그 서브클래스가 아니라면 null 반환
     * @param requestClass
     * @param request
     * @return
     */
    private static HttpServletRequest extractRequest(Class<? extends HttpServletRequest> requestClass, ServletRequest request) {
        if (requestClass.isInstance(request)) {
            return (HttpServletRequest) request;
        } else if (request instanceof HttpServletRequestWrapper) {
            return extractRequest(requestClass, ((HttpServletRequestWrapper) request).getRequest());
        } else {
            return null;
        }
    }

}
