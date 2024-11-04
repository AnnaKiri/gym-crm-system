package com.annakirillova.crmsystem.filter;

import com.annakirillova.crmsystem.config.SecurityConfig;
import com.annakirillova.crmsystem.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.io.PrintWriter;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenValidationFilterUnitTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TokenValidationFilter tokenValidationFilter;

    private static final String VALID_TOKEN = SecurityConfig.BEARER_PREFIX + "validToken";
    private static final String INVALID_TOKEN = SecurityConfig.BEARER_PREFIX + "invalidToken";

    @BeforeEach
    void setUp() {
        tokenValidationFilter = new TokenValidationFilter(tokenService);
    }

    @Test
    void validToken() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(VALID_TOKEN);
        when(tokenService.isTokenInvalid(anyString())).thenReturn(false);

        tokenValidationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void invalidToken() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(INVALID_TOKEN);
        when(tokenService.isTokenInvalid(anyString())).thenReturn(true);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        tokenValidationFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer, times(1)).write("Token is invalid");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void noAuthorizationHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        tokenValidationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void noBearerToken() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic someToken");

        tokenValidationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
