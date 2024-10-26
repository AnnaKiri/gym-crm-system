package com.annakirillova.crmsystem.service;

import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.crmsystem.exception.AppException;
import com.annakirillova.crmsystem.exception.AuthenticationException;
import com.annakirillova.crmsystem.exception.FeignServiceUnavailableException;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.feign.KeycloakAuthFeignClient;
import feign.FeignException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.stream.Stream;

import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakAuthFeignClientHelperUnitTest {

    @Mock
    private KeycloakAuthFeignClient keycloakAuthFeignClient;

    @InjectMocks
    private KeycloakAuthFeignClientHelper keycloakAuthFeignClientHelper;

    private Map<String, String> formData;

    @BeforeEach
    void setUp() {
        formData = Map.of(
                "client_id", "test-client",
                "client_secret", "test-secret",
                "username", "test-user",
                "password", "test-password"
        );
    }

    @Test
    void requestTokenSuccess() {
        when(keycloakAuthFeignClient.loginUser(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);

        TokenResponseDto response = keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(formData);

        verify(keycloakAuthFeignClient, times(1)).loginUser(any(Map.class));

        assertEquals(TOKEN_RESPONSE_DTO, response);
    }

    @Test
    void logoutSuccess() {
        when(keycloakAuthFeignClient.logoutUser(any(Map.class))).thenReturn(ResponseEntity.ok().build());

        keycloakAuthFeignClientHelper.logoutUserWithCircuitBreaker(formData);

        verify(keycloakAuthFeignClient, times(1)).logoutUser(any(Map.class));
    }

    static Stream<Arguments> exceptionProvider() {
        return Stream.of(
                Arguments.of(400, ValidationException.class),
                Arguments.of(401, AuthenticationException.class),
                Arguments.of(403, AccessDeniedException.class),
                Arguments.of(404, NotFoundException.class),
                Arguments.of(500, AppException.class),
                Arguments.of(503, FeignServiceUnavailableException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void requestTokenFallbackFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakAuthFeignClientHelper.loginFallback(formData, feignException));
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void logoutUserFallbackFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakAuthFeignClientHelper.logoutFallback(formData, feignException));
    }
}
