package com.assetflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "assetflow")
public class AppProperties {
    
    private final Security security = new Security();

    @Getter
    @Setter
    public static class Security {
        private final Jwt jwt = new Jwt();

        @Getter
        @Setter
        public static class Jwt {
            private String secret;
            private long expirationMs;
            private long refreshExpirationMs;
        }
    }
}
