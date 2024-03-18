package com.boot.redis.config.filter;

import com.boot.redis.open.OpenApi;
import com.boot.redis.open.service.OpenApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    private final String API_KEY_HEADER = "Authentication";

    private final OpenApiService openApiService;
    private final ObjectMapper objectMapper;

    private String identity;

    public ApiKeyFilter(OpenApiService openApiService) {
        this.openApiService = openApiService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("---------------------------------------- Once Filter Activated ----------------------------------------");

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            boolean check = checkApiKey(request, response);
            if(!check) {
                return;
            }

            filterChain.doFilter(request, response);
        }catch (Exception e) {
            log.error("Error in Once Filter", e);
            responseError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", request, response);
            return;
        }

        // TODO: After Logging Logic


        log.info("---------------------------------------- Once Filter END ----------------------------------------");
    }

    private boolean checkApiKey(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String reqApiKey = request.getHeader(HttpHeaders.AUTHORIZATION);

        HttpHeaders httpHeaders = new HttpHeaders();

        //sendError
        if(reqApiKey == null || reqApiKey.isEmpty()) {
            responseError(HttpServletResponse.SC_UNAUTHORIZED, "API Key is missing", request, response);
            return false;
        } else {
            OpenApi openApi = openApiService.getApiKey(reqApiKey);

            if(openApi == null) {
                responseError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key", request, response);
                return false;
            }

            LocalDate exprDate = LocalDate.parse(openApi.getApiMngrExprDate(), formatter);
            LocalDate today = LocalDate.now();

            identity = openApi.getApiMngrName();

            if(reqApiKey.equals(openApi.getApiKey()) ) {
                // Check if the API Key is expired
                if(today.isAfter(exprDate)) {
                    responseError(HttpServletResponse.SC_FORBIDDEN, "API Key is expired", request, response);
                    return false;
                }

            } else {
                responseError(HttpServletResponse.SC_FORBIDDEN, "Is not Correct API Key",request ,response);
            }
        }
        return true;
    }


    private void responseError(int status, String message, HttpServletRequest request ,HttpServletResponse response) throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("code", String.valueOf(status));
        map.put("message", message);

        log.warn("OpenAPI Response Error: identity = {},  body = {}", identity, map);

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        this.objectMapper.writeValue(response.getWriter(), map);
    }
}
