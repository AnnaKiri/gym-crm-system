package com.annakirillova.crmsystem.integration;

import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO_MATCHER;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.BLOCK_DURATION_MINUTES;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.MAX_ATTEMPTS;
import static com.annakirillova.crmsystem.web.AuthController.REST_URL;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void authenticateSuccess() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TOKEN_RESPONSE_DTO_MATCHER.contentJson(TOKEN_RESPONSE_DTO));
    }

    @Test
    void authenticateBlockedUser() throws Exception {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setAttempts(MAX_ATTEMPTS);
        loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutSuccess() throws Exception {
        Jwt jwt = Jwt.withTokenValue("access-token")
                .header("alg", "HS256")
                .claim("sub", "user1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .with(jwt().jwt(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TOKEN_RESPONSE_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
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
        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
