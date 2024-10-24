package com.annakirillova.crmsystem.integration;

import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static com.annakirillova.crmsystem.UserTestData.USERS_PASSWORDS;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.BLOCK_DURATION_MINUTES;
import static com.annakirillova.crmsystem.service.BruteForceProtectionService.MAX_ATTEMPTS;
import static com.annakirillova.crmsystem.web.AuthController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void authenticateSuccess() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(USER_1_USERNAME, USERS_PASSWORDS.get(USER_1_USERNAME));

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        TokenResponseDto tokenResponseDto = JsonUtil.readValue(action.andReturn().getResponse().getContentAsString(), TokenResponseDto.class);
        assertNotNull(tokenResponseDto);
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
        TokenResponseDto tokenResponseDto = getTokensForUser(USER_1);

        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + tokenResponseDto.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(tokenResponseDto)))
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
