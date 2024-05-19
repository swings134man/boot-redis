package com.boot.redis.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.config")
@Getter
public class ApiGenProperties {

    private final Api api = new Api();

    public static class Api {
        @Getter
        @Setter
        private String encryptKey;

        @Getter
        @Setter
        private String ivKey;
    }


}
