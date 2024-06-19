package com.boot.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/************
 * @info :
 * @name : ApiServerConfig
 * @date : 2024. 6. 19. 오후 8:50
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiServerConfig {

    @NestedConfigurationProperty
    public final ServerProperties server = new ServerProperties();

    @Getter @Setter
    public static class ServerProperties {
        // 서버 접근 허용 ip
        private List<String> accessIpAddress = new ArrayList<>();

        private Boolean isFileServer = true;

        private String fileServerUrl;

        //서버 url
        private String url;

        //[must] 테스트전 반드시 환경에 맞춰서 값을 설정한다.
        //openapi user secure key ( 유저 생성시 최초 한번 발급 받는다 )
        private String secretKey;

        private String userId;
    }
}
