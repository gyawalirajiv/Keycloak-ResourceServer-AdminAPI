package com.example.keycloakdemo.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminAPIConfig {
    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8085")
                .realm("your-realm-name")
                .clientId("your-client-id")
                .grantType(OAuth2Constants.PASSWORD)
                .username("username")
                .password("password")
                .build();
    }


}
