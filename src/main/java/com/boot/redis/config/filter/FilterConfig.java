package com.boot.redis.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<ApiKeyFilterTest> filterBean(ApiKeyFilterTest apiKeyFilterTest) {
        FilterRegistrationBean<ApiKeyFilterTest> filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(apiKeyFilterTest);
        filterRegBean.addUrlPatterns("/open/*");

        return filterRegBean;
    }

}
