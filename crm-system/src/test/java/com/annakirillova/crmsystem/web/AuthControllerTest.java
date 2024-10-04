package com.annakirillova.crmsystem.web;

import com.annakirillova.crmsystem.BaseTest;
import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.repository.LoginAttemptRepository;
import com.annakirillova.crmsystem.service.KeycloakAuthFeignClientHelper;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO_MATCHER;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.BLOCK_DURATION_MINUTES;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.MAX_ATTEMPTS;
import static com.annakirillova.crmsystem.web.AuthController.REST_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private LoginAttemptRepository loginAttemptRepository;

    @MockBean
    private KeycloakAuthFeignClientHelper keycloakAuthFeignClientHelper;

    @Test
    void authenticateSuccess() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("user1", "password1");
        TokenResponseDto tokenResponseDto = new TokenResponseDto("access-token", 5L, "refresh-token");

        when(loginAttemptRepository.findByUsername(loginRequestDto.getUsername())).thenReturn(Optional.empty());
        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(tokenResponseDto);
        when(loginAttemptRepository.deleteByUsername(loginRequestDto.getUsername())).thenReturn(1);

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TOKEN_RESPONSE_DTO_MATCHER.contentJson(tokenResponseDto));

        verify(loginAttemptRepository, times(1)).findByUsername(loginRequestDto.getUsername());
        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(any(Map.class));
        verify(loginAttemptRepository, times(1)).deleteByUsername(loginRequestDto.getUsername());
    }

    @Test
    void authenticateBlockedUser() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("blockedUser", "password1");
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setAttempts(MAX_ATTEMPTS);
        loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));

        when(loginAttemptRepository.findByUsername(loginRequestDto.getUsername()))
                .thenReturn(Optional.of(loginAttempt));

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(loginAttemptRepository, times(1)).findByUsername(loginRequestDto.getUsername());
    }

    @Test
    void logoutSuccess() throws Exception {
        String accessToken = "access-token";
        TokenResponseDto tokenResponseDto = new TokenResponseDto("access-token", 5L, "refresh-token");

        Jwt jwt = Jwt.withTokenValue(accessToken)
                .header("alg", "HS256")
                .claim("sub", "user1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(eq("access-token"), eq("invalidated"), eq(3600L), eq(TimeUnit.SECONDS));
        doNothing().when(keycloakAuthFeignClientHelper).logoutUserWithCircuitBreaker(any(Map.class));

        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .with(jwt().jwt(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(tokenResponseDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(jwtDecoder, times(1)).decode(anyString());
        verify(valueOperations, times(1)).set(eq("access-token"), eq("invalidated"), eq(3600L), eq(TimeUnit.SECONDS));
        verify(keycloakAuthFeignClientHelper, times(1)).logoutUserWithCircuitBreaker(any(Map.class));
    }

    @Test
    void logoutInvalidToken() throws Exception {
        String invalidToken = "InvalidToken";
        TokenResponseDto tokenResponseDto = new TokenResponseDto("access-token", 5L, "refresh-token");

        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .header(HttpHeaders.AUTHORIZATION, invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(tokenResponseDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateWrongCredentials() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("user1", "wrongPassword");

        when(loginAttemptRepository.findByUsername(loginRequestDto.getUsername())).thenReturn(Optional.empty());
        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class)))
                .thenThrow(new BadCredentialsException("Wrong credentials"));

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(loginAttemptRepository, times(1)).findByUsername(loginRequestDto.getUsername());
        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(any(Map.class));
    }
}
