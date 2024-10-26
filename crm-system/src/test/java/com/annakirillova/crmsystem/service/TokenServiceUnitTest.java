package com.annakirillova.crmsystem.service;

import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.crmsystem.config.KeycloakProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.annakirillova.crmsystem.FeignClientTestData.ADMIN_PROPERTIES;
import static com.annakirillova.crmsystem.FeignClientTestData.JWT_DUMB;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.USER_PROPERTIES;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceUnitTest {

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private KeycloakAuthFeignClientHelper keycloakAuthFeignClientHelper;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void invalidateTokenSuccess() {
        String token = JWT_DUMB.getTokenValue();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(eq(token), eq("invalidated"), anyLong(), eq(TimeUnit.SECONDS));

        when(jwtDecoder.decode(token)).thenReturn(JWT_DUMB);

        tokenService.invalidateToken(token);

        verify(valueOperations, times(1)).set(eq(token), eq("invalidated"), any(Long.class), eq(TimeUnit.SECONDS));
    }

    @Test
    void isTokenInvalidTrue() {
        String token = JWT_DUMB.getTokenValue();
        when(redisTemplate.hasKey(token)).thenReturn(true);

        boolean isInvalid = tokenService.isTokenInvalid(token);

        assertTrue(isInvalid);
        verify(redisTemplate, times(1)).hasKey(token);
    }

    @Test
    void getAdminTokenSuccess() {
        when(keycloakProperties.getAdmin()).thenReturn(ADMIN_PROPERTIES);

        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);

        String adminToken = tokenService.getAdminToken();

        assertEquals(TOKEN_RESPONSE_DTO.getAccessToken(), adminToken);
        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(any(Map.class));
    }

    @Test
    void getUserTokenSuccess() {
        when(keycloakProperties.getUser()).thenReturn(USER_PROPERTIES);

        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);

        TokenResponseDto userToken = tokenService.getUserToken(USER_1_USERNAME, "password");

        assertEquals(TOKEN_RESPONSE_DTO.getAccessToken(), userToken.getAccessToken());
        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(any(Map.class));
    }

    @Test
    void logoutUserSuccess() {
        when(keycloakProperties.getUser()).thenReturn(USER_PROPERTIES);

        doNothing().when(keycloakAuthFeignClientHelper).logoutUserWithCircuitBreaker(any(Map.class));

        tokenService.logoutUser(TOKEN_RESPONSE_DTO.getRefreshToken());

        verify(keycloakAuthFeignClientHelper, times(1)).logoutUserWithCircuitBreaker(any(Map.class));
    }

    @Test
    void getExpirationTimeSeconds() {
        String token = JWT_DUMB.getTokenValue();
        Instant expiration = Instant.now().plusSeconds(3600);

        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("exp", expiration)
                .build();

        when(jwtDecoder.decode(token)).thenReturn(jwt);

        long expirationTime = tokenService.getExpirationTimeSeconds(token);

        assertEquals(expiration.getEpochSecond(), expirationTime);
    }

    @Test
    void getTimeRemaining() {
        String token = "sampleToken";
        Instant expiration = Instant.now().plusSeconds(3600);

        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("exp", expiration)
                .build();

        when(jwtDecoder.decode(token)).thenReturn(jwt);

        long timeRemaining = tokenService.getTimeRemaining(token);

        assertTrue(timeRemaining > 0);
    }
}
