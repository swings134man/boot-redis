//package com.boot.redis.config.filter;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class ApiKeyFilter extends OncePerRequestFilter {
//
//    private String API_KEY_HEADER = "Authentication";
//
//    private final String apiKey;
//
//    public ApiKeyFilter(String apiKey) {
//        this.apiKey = apiKey;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
//        String requestApiKey = request.getHeader(API_KEY_HEADER);
//        if (apiKey.equals(requestApiKey)) {
//            filterChain.doFilter(request, response);
//        } else {
//            response.sendError(HttpStatus.FORBIDDEN.value(), "Invalid API Key");
//        }
//    }
//}
