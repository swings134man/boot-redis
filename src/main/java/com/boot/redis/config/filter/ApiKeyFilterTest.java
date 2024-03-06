package com.boot.redis.config.filter;

import com.boot.redis.open.OpenApi;
import com.boot.redis.open.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyFilterTest implements Filter {

    private final OpenApiService openApiService;

    private final String API_KEY_HEADER = "Authentication";

    @Override
    public void init(final javax.servlet.FilterConfig filterConfig) throws ServletException {
        log.info("filter init~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~.");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("filter doFilter~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~.");
        log.info("Filter START");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        OpenApi openApi = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");


        log.info("HEADER:: {}", httpRequest.getHeader(API_KEY_HEADER));
        String reqApiKey = httpRequest.getHeader(API_KEY_HEADER);

        if(reqApiKey == null || reqApiKey.isEmpty()) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key is missing");
            return;
        }else {
            openApi = openApiService.getApiKey(reqApiKey);

            if(openApi == null) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key");
                return;
            }

            LocalDate exprDate = LocalDate.parse(openApi.getApiMngrExprDate(), formatter);
            LocalDate today = LocalDate.now();

            if(reqApiKey.equals(openApi.getApiKey()) ) {
                if(today.isAfter(exprDate)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "API Key is expired");
                    return;
                }

                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN, "Is not Correct API Key");
            }
        }


        log.info("Filter END");
    }
}
