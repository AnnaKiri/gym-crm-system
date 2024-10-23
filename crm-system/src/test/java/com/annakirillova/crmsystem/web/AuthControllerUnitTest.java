package com.annakirillova.crmsystem.web;

import com.annakirillova.crmsystem.models.LoginAttempt;
import com.annakirillova.crmsystem.service.AuthService;
import com.annakirillova.crmsystem.service.BruteForceProtectionService;
import com.annakirillova.crmsystem.service.TokenService;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerUnitTest extends BaseControllerUnitTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthService authService;

    @Test
    void authenticateSuccess() throws Exception {
        when(bruteForceProtectionService.isBlocked(LOGIN_REQUEST_DTO.getUsername())).thenReturn(false);
        when(tokenService.getUserToken(LOGIN_REQUEST_DTO.getUsername(), LOGIN_REQUEST_DTO.getPassword())).thenReturn(TOKEN_RESPONSE_DTO);
        doNothing().when(bruteForceProtectionService).resetBlock(LOGIN_REQUEST_DTO.getUsername());

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TOKEN_RESPONSE_DTO_MATCHER.contentJson(TOKEN_RESPONSE_DTO));

        verify(bruteForceProtectionService, times(1)).isBlocked(LOGIN_REQUEST_DTO.getUsername());
        verify(tokenService, times(1)).getUserToken(LOGIN_REQUEST_DTO.getUsername(), LOGIN_REQUEST_DTO.getPassword());
        verify(bruteForceProtectionService, times(1)).resetBlock(LOGIN_REQUEST_DTO.getUsername());
    }

    @Test
    void authenticateBlockedUser() throws Exception {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setAttempts(MAX_ATTEMPTS);
        loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));

        when(bruteForceProtectionService.isBlocked(LOGIN_REQUEST_DTO.getUsername())).thenReturn(true);

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(bruteForceProtectionService, times(1)).isBlocked(LOGIN_REQUEST_DTO.getUsername());
    }

    @Test
    void logoutSuccess() throws Exception {
        Jwt jwt = Jwt.withTokenValue("access-token")
                .header("alg", "HS256")
                .claim("sub", "user1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(authService.getJwtToken()).thenReturn("access-token");
        doNothing().when(tokenService).invalidateToken(any(String.class));
        doNothing().when(tokenService).logoutUser(any(String.class));

        perform(MockMvcRequestBuilders.post(REST_URL + "/logout")
                .with(jwt().jwt(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TOKEN_RESPONSE_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(authService, times(1)).getJwtToken();
        verify(tokenService, times(1)).invalidateToken(any(String.class));
        verify(tokenService, times(1)).logoutUser(any(String.class));
    }

    @Test
    void authenticateWrongCredentials() throws Exception {
        when(bruteForceProtectionService.isBlocked(LOGIN_REQUEST_DTO.getUsername())).thenReturn(false);
        when(tokenService.getUserToken(LOGIN_REQUEST_DTO.getUsername(), LOGIN_REQUEST_DTO.getPassword())).thenThrow(BadCredentialsException.class);

        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LOGIN_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(bruteForceProtectionService, times(1)).isBlocked(LOGIN_REQUEST_DTO.getUsername());
        verify(tokenService, times(1)).getUserToken(LOGIN_REQUEST_DTO.getUsername(), LOGIN_REQUEST_DTO.getPassword());
    }
}
