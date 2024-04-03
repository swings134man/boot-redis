package com.boot.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .antMatcher("/**")
//                .httpBasic();
//        return http.build();
        http.authorizeRequests()
                .anyRequest().permitAll() // 모든 요청에 대해 인증을 요구하지 않음
                .and()
                .csrf().disable(); // CSRF 보호를 비활성화

        http.headers()
//                .contentSecurityPolicy("frame-ancestors 'self' https://*.premiumoutlets.co.kr").and()
                .frameOptions()
                .deny();

        return http.build();
    }

}
