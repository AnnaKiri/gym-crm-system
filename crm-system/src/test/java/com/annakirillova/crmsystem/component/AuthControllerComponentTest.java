package com.annakirillova.crmsystem.component;

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

import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO_MATCHER;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.BLOCK_DURATION_MINUTES;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.MAX_ATTEMPTS;
import static com.annakirillova.crmsystem.web.AuthController.REST_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

public class AuthControllerComponentTest extends BaseControllerComponentTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private LoginAttemptRepository loginAttemptRepository;

    @MockBean
    private KeycloakAuthFeignClientHelper keycloakAuthFeignClientHelper;

    @Test
    void authenticateSuccess() throws Exception {
        when(loginAttemptRepository.findByUsername(LOGIN_REQUEST_DTO.getUsername())).thenReturn(Optional.empty());
        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);
        when(loginAttemptRepository.deleteByUsername(LOGIN_REQUEST_DTO.getUsername())).thenReturn(1);

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TOKEN_RESPONSE_DTO_MATCHER.contentJson(TOKEN_RESPONSE_DTO));

        verify(loginAttemptRepository, times(1)).findByUsername(LOGIN_REQUEST_DTO.getUsername());
        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(any(Map.class));
        verify(loginAttemptRepository, times(1)).deleteByUsername(LOGIN_REQUEST_DTO.getUsername());
    }

    @Test
    void authenticateBlockedUser() throws Exception {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setAttempts(MAX_ATTEMPTS);
        loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));

        when(loginAttemptRepository.findByUsername(LOGIN_REQUEST_DTO.getUsername()))
                .thenReturn(Optional.of(loginAttempt));

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(loginAttemptRepository, times(1)).findByUsername(LOGIN_REQUEST_DTO.getUsername());
    }

    @Test
    void logoutSuccess() throws Exception {
        Jwt jwt = Jwt.withTokenValue("access-token")
                .header("alg", "HS256")
                .claim("sub", "user1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(eq("access-token"), eq("invalidated"), anyLong(), eq(TimeUnit.SECONDS));
        doNothing().when(keycloakAuthFeignClientHelper).logoutUserWithCircuitBreaker(any(Map.class));

        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .with(jwt().jwt(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TOKEN_RESPONSE_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(jwtDecoder, times(1)).decode(anyString());
        verify(valueOperations, times(1)).set(eq("access-token"), eq("invalidated"), anyLong(), eq(TimeUnit.SECONDS));
        verify(keycloakAuthFeignClientHelper, times(1)).logoutUserWithCircuitBreaker(any(Map.class));
    }

    @Test
    void logoutInvalidToken() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .header(HttpHeaders.AUTHORIZATION, "InvalidToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TOKEN_RESPONSE_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateWrongCredentials() throws Exception {
        when(loginAttemptRepository.findByUsername(LOGIN_REQUEST_DTO.getUsername())).thenReturn(Optional.empty());
        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class)))
                .thenThrow(new BadCredentialsException("Wrong credentials"));

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(loginAttemptRepository, times(1)).findByUsername(LOGIN_REQUEST_DTO.getUsername());
        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(any(Map.class));
    }
}
