package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.config.KeycloakProperties;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
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
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtDecoder jwtDecoder;
    private final KeycloakAuthFeignClientHelper keycloakAuthFeignClientHelper;

    public void invalidateToken(String token) {
        log.debug("Token {} has been marked as invalid.", token);
        redisTemplate.opsForValue().set(token, "invalidated", getTimeRemaining(token), TimeUnit.SECONDS);
    }

    public boolean isTokenInvalid(String token) {
        log.debug("Checking if token {} is in the invalidated token storage.", token.substring(1, 10));
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public TokenResponseDto getAdminToken() {
        Map<String, String> formData = createAuthForm(
                keycloakProperties.getAdmin().getClientId(),
                keycloakProperties.getAdmin().getClientSecret(),
                keycloakProperties.getAdmin().getUsername(),
                keycloakProperties.getAdmin().getPassword()
        );

        return requestToken(formData, "Admin token");
    }

    public TokenResponseDto getOrdinaryToken(String username, String password) {
        Map<String, String> formData = createAuthForm(
                keycloakProperties.getUser().getClientId(),
                keycloakProperties.getUser().getClientSecret(),
                username,
                password
        );

        return requestToken(formData, "Token for user " + username);
    }

    public void logoutUser(String refreshToken) {
        Map<String, String> request = createLogoutRequest(refreshToken);

        try {
            keycloakAuthFeignClientHelper.logoutUserWithCircuitBreaker(request).get();
            log.info("User with refresh token {} logged out successfully.", refreshToken.substring(1, 10));
        } catch (Exception e) {
            throw new KeycloakOperationException("Failed to log out user: " + e.getMessage());
        }
    }

    public long getExpirationTimeSeconds(String token) {
        Jwt decodedJwt = jwtDecoder.decode(token);
        Instant expiration = decodedJwt.getExpiresAt();

        log.debug("Token {} expires at {}", token.substring(1, 10), expiration);
        return expiration.getEpochSecond();
    }

    public long getTimeRemaining(String token) {
        long expirationTimeSeconds = getExpirationTimeSeconds(token);
        long currentTimeSeconds = Instant.now().getEpochSecond();
        long timeRemaining = expirationTimeSeconds - currentTimeSeconds;
        log.debug("Time remaining for token {}: {} seconds", token.substring(1, 10), timeRemaining);
        return timeRemaining;
    }

    private TokenResponseDto requestToken(Map<String, String> formData, String logMessage) {
        TokenResponseDto tokenResponse;
        try {
            tokenResponse = keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(formData).get();
            ;
            log.info("{} received successfully.", logMessage);
        } catch (Exception e) {
            log.error("Failed to retrieve {}.", logMessage, e);
            throw new KeycloakOperationException("Failed to retrieve. " + logMessage);
        }

        return tokenResponse;
    }

    private Map<String, String> createAuthForm(String clientId, String clientSecret, String username, String password) {
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("grant_type", "password");
        formData.put("username", username);
        formData.put("password", password);
        return formData;
    }

    private Map<String, String> createLogoutRequest(String refreshToken) {
        Map<String, String> request = new HashMap<>();
        request.put("client_id", keycloakProperties.getUser().getClientId());
        request.put("client_secret", keycloakProperties.getUser().getClientSecret());
        request.put("refresh_token", refreshToken);
        return request;
    }
}
