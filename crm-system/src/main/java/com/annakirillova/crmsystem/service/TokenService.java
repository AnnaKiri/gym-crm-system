package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.feign.KeycloakAuthFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String ordinaryClientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String ordinaryClientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    private final KeycloakAuthFeignClient keycloakAuthFeignClient;

    public TokenResponseDto getAdminToken() {
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", adminClientId);
        formData.put("client_secret", adminClientSecret);
        formData.put("grant_type", "password");
        formData.put("username", adminUsername);
        formData.put("password", adminPassword);

        return keycloakAuthFeignClient.loginUser(formData);
    }

    public TokenResponseDto getOrdinaryToken(String username, String password) {
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", ordinaryClientId);
        formData.put("client_secret", ordinaryClientSecret);
        formData.put("grant_type", "password");
        formData.put("username", username);
        formData.put("password", password);

        return keycloakAuthFeignClient.loginUser(formData);
    }
}
