package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
import com.annakirillova.crmsystem.feign.KeycloakAuthFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthFeignClientHelper {

    private final KeycloakAuthFeignClient keycloakAuthFeignClient;

    @CircuitBreaker(name = "keycloakAuthService", fallbackMethod = "loginFallback")
    public TokenResponseDto requestTokenWithCircuitBreaker(Map<String, ?> formData) {
        return keycloakAuthFeignClient.loginUser(formData);
    }

    public TokenResponseDto loginFallback(Map<String, ?> formData, Throwable throwable) {
        log.error("Keycloak login failed: {}", throwable.getMessage());
        throw new KeycloakOperationException("Keycloak login failed: " + throwable.getMessage());
    }

    @CircuitBreaker(name = "keycloakAuthService", fallbackMethod = "logoutFallback")
    public void logoutUserWithCircuitBreaker(Map<String, ?> request) {
        keycloakAuthFeignClient.logoutUser(request);
    }

    public void logoutFallback(Map<String, ?> request, Throwable throwable) {
        log.error("Keycloak logout failed: {}", throwable.getMessage());
        throw new KeycloakOperationException("Keycloak logout failed: " + throwable.getMessage());
    }
}
