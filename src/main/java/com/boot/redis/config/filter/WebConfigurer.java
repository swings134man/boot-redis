package com.boot.redis.config.filter;

import com.boot.redis.open.service.OpenApiService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    private final OpenApiService openApiService;

    public WebConfigurer(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    // for Test -> Allow *
    @Bean
    public FilterRegistrationBean<CorsFilter> corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(false);
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            config.setMaxAge(6000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean(new CorsFilter(source));
        filterBean.setOrder(0);

        return filterBean;
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

    //FIXME: Security Filter 보다 먼저 실행되어야 함
    @Bean
    public FilterRegistrationBean requestCachingFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new RequestCachingFilter());
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        filterRegBean.setUrlPatterns(urlPatterns);
        filterRegBean.setOrder(1);
        filterRegBean.setAsyncSupported(true);
        return filterRegBean;
    }

}
