package com.annakirillova.trainerworkloadservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakPropertiesExtended {

    private Admin admin = new Admin();
    private User user = new User();
    private String url;
    private String realm;

    @Data
    public static class Admin {
        private String username;
        private String password;
        private String clientId;
        private String clientSecret;
    }

    @Data
    public static class User {
        private String clientId;
        private String clientSecret;
    }
}
