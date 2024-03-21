package com.boot.redis.config.filter;

import com.boot.redis.open.service.OpenApiService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    private final OpenApiService openApiService;

    public FilterConfig(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @Bean
    public FilterRegistrationBean apiFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new ApiKeyFilter(openApiService));
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/api/open/*");
        filterRegBean.setUrlPatterns(urlPatterns);
        return filterRegBean;
    }

}
