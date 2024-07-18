package com.boot.redis.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logging Filter
 * - ContentCachingRequestWrapper: HttpServletRequest 본문을 여러번 읽을 수 있도록 캐싱.
 *
 * - 일반적으로 HttpServletRequest는 InputStream을 한번 읽으면 다시 읽을 수 없다.
 * 하지만, 요청데이터 검증, 로깅 등을 위해 본문을 여러번 읽어야 할 때가 있다.
 */
@Slf4j
public class RequestCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        // TEST: Logging Request
//        log.info("@@@@ HttpServletRequest Body = {}", new String(requestWrapper.getContentAsByteArray()));

        // 원래 응답 내용 복구
        responseWrapper.copyBodyToResponse();
    }
}
