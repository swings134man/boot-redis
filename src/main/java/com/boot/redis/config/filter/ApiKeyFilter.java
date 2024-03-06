package com.boot.redis.config.filter;

import com.boot.redis.open.OpenApi;
import com.boot.redis.open.service.OpenApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    private final String API_KEY_HEADER = "Authentication";

    private OpenApiService openApiService;

    public ApiKeyFilter(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("---------------------------------------- Once Filter Activated ----------------------------------------");

        log.info("Once Filter Request = {}", request.getRequestURL());

        try {
            checkApiKey(request, response);

            filterChain.doFilter(request, response);
        }catch (Exception e) {
            log.error("Error in Once Filter", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return;
        }


        log.info("---------------------------------------- Once Filter END ----------------------------------------");
    }

    private void checkApiKey(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Filter START");

        OpenApi openApi = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        log.info("HEADER:: {}", request.getHeader(API_KEY_HEADER));
        String reqApiKey = request.getHeader(API_KEY_HEADER);

        if(reqApiKey == null || reqApiKey.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key is missing");
            return;
        }else {
            openApi = openApiService.getApiKey(reqApiKey);

            if(openApi == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key");
                return;
            }

            LocalDate exprDate = LocalDate.parse(openApi.getApiMngrExprDate(), formatter);
            LocalDate today = LocalDate.now();

            if(reqApiKey.equals(openApi.getApiKey()) ) {
                if(today.isAfter(exprDate)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "API Key is expired");
                    return;
                }


            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN, "Is not Correct API Key");
            }
        }


        log.info("Filter END");
    }
}
