package com.annakirillova.crmsystem.feign;

import com.annakirillova.crmsystem.config.FeignClientConfig;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "keycloak-auth", url = "${keycloak.url}/realms/${keycloak.realm}/protocol/openid-connect", configuration = FeignClientConfig.class)
public interface KeycloakAuthFeignClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @CircuitBreaker(name = "keycloak")
    @TimeLimiter(name = "keycloak")
    TokenResponseDto loginUser(Map<String, ?> request);

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @CircuitBreaker(name = "keycloak")
    @TimeLimiter(name = "keycloak")
    ResponseEntity<Void> logoutUser(Map<String, ?> request);
}
