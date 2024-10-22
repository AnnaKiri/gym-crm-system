package com.annakirillova.crmsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getJwtToken() {
        Jwt jwtMock = mock(Jwt.class);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwtMock);
        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtMock.getTokenValue()).thenReturn("mock-token");

        String token = authService.getJwtToken();

        assertEquals("mock-token", token);
        verify(securityContext).getAuthentication();
    }

    @Test
    void noAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        String token = authService.getJwtToken();

        assertNull(token);
        verify(securityContext).getAuthentication();
    }

    @Test
    void getUsernameFromToken() {
        Jwt jwtMock = mock(Jwt.class);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwtMock);
        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtMock.getClaimAsString("preferred_username")).thenReturn("mock-username");

        String username = authService.getUsername();

        assertEquals("mock-username", username);
        verify(securityContext).getAuthentication();
    }

    @Test
    void getUsernameNull() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        String username = authService.getUsername();

        assertNull(username);
        verify(securityContext).getAuthentication();
    }
}
