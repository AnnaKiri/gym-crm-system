package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
import com.annakirillova.crmsystem.feign.KeycloakAuthFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthFeignClientHelper {

    private final KeycloakAuthFeignClient keycloakAuthFeignClient;

    @CircuitBreaker(name = "keycloakAuthService", fallbackMethod = "loginFallback")
    @TimeLimiter(name = "keycloakAuthService")
    public CompletableFuture<TokenResponseDto> requestTokenWithCircuitBreaker(Map<String, ?> formData) {
        return CompletableFuture.supplyAsync(() -> keycloakAuthFeignClient.loginUser(formData));
    }

    public CompletableFuture<TokenResponseDto> loginFallback(Map<String, ?> formData, Throwable throwable) {
        log.error("Keycloak login failed: {}", throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Keycloak login failed: " + throwable.getMessage())
        );
    }

    @CircuitBreaker(name = "keycloakAuthService", fallbackMethod = "logoutFallback")
    @TimeLimiter(name = "keycloakAuthService")
    public CompletableFuture<Void> logoutUserWithCircuitBreaker(Map<String, ?> request) {
        return CompletableFuture.runAsync(() -> keycloakAuthFeignClient.logoutUser(request));
    }

    public CompletableFuture<Void> logoutFallback(Map<String, ?> request, Throwable throwable) {
        log.error("Keycloak logout failed: {}", throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Keycloak logout failed: " + throwable.getMessage())
        );
    }
}
