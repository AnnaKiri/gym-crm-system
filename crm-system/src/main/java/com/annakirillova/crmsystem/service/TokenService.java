package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.config.KeycloakProperties;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.feign.KeycloakAuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final KeycloakProperties keycloakProperties;
    private final KeycloakAuthFeignClient keycloakAuthFeignClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtDecoder jwtDecoder;

    public void invalidateToken(String token) {
        log.debug("Token {} has been marked as invalid.", token);
        redisTemplate.opsForValue().set(token, "invalidated", getTimeRemaining(token), TimeUnit.SECONDS);
    }

    public boolean isTokenInvalid(String token) {
        log.debug("Checking if token {} is in the invalidated token storage.", token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public TokenResponseDto getAdminToken() {
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", keycloakProperties.getAdmin().getClientId());
        formData.put("client_secret", keycloakProperties.getAdmin().getClientSecret());
        formData.put("grant_type", "password");
        formData.put("username", keycloakProperties.getAdmin().getUsername());
        formData.put("password", keycloakProperties.getAdmin().getPassword());

        return keycloakAuthFeignClient.loginUser(formData);
    }

    public TokenResponseDto getOrdinaryToken(String username, String password) {
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", keycloakProperties.getUser().getClientId());
        formData.put("client_secret", keycloakProperties.getUser().getClientSecret());
        formData.put("grant_type", "password");
        formData.put("username", username);
        formData.put("password", password);

        return keycloakAuthFeignClient.loginUser(formData);
    }

    public void logoutUser(String refreshToken) {
        Map<String, String> request = new HashMap<>();
        request.put("client_id", keycloakProperties.getUser().getClientId());
        request.put("client_secret", keycloakProperties.getUser().getClientSecret());
        request.put("refresh_token", refreshToken);

        keycloakAuthFeignClient.logoutUser(request);
    }

    public long getExpirationTimeSeconds(String token) {
        Jwt decodedJwt = jwtDecoder.decode(token);
        Instant expiration = decodedJwt.getExpiresAt();
        return expiration.getEpochSecond();
    }

    public long getTimeRemaining(String token) {
        long expirationTimeSeconds = getExpirationTimeSeconds(token);
        long currentTimeSeconds = Instant.now().getEpochSecond();
        return expirationTimeSeconds - currentTimeSeconds;
    }
}
