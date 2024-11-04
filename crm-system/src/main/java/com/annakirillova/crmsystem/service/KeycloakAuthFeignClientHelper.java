package com.annakirillova.crmsystem.service;

import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.crmsystem.feign.KeycloakAuthFeignClient;
import com.annakirillova.crmsystem.util.FeignExceptionUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthFeignClientHelper {

    private static final String SERVICE_NAME = "Keycloak Open Id API Service";

    private final KeycloakAuthFeignClient keycloakAuthFeignClient;

    @CircuitBreaker(name = "keycloakAuthService", fallbackMethod = "loginFallback")
    public TokenResponseDto requestTokenWithCircuitBreaker(Map<String, ?> formData) {
        return keycloakAuthFeignClient.loginUser(formData);
    }

    public TokenResponseDto loginFallback(Map<String, ?> formData, Throwable throwable) throws Throwable {
        Map<String, String> exceptionMessages = FeignExceptionUtil.getExceptionMessages(SERVICE_NAME, throwable);
        throw FeignExceptionUtil.handleFeignException(throwable, exceptionMessages);
    }

    @CircuitBreaker(name = "keycloakAuthService", fallbackMethod = "logoutFallback")
    public void logoutUserWithCircuitBreaker(Map<String, ?> request) {
        keycloakAuthFeignClient.logoutUser(request);
    }

    public void logoutFallback(Map<String, ?> request, Throwable throwable) throws Throwable {
        Map<String, String> exceptionMessages = FeignExceptionUtil.getExceptionMessages(SERVICE_NAME, throwable);
        throw FeignExceptionUtil.handleFeignException(throwable, exceptionMessages);
    }
}
