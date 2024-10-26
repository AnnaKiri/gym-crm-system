package com.annakirillova.crmsystem.service;

import com.annakirillova.common.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.exception.AppException;
import com.annakirillova.crmsystem.exception.AuthenticationException;
import com.annakirillova.crmsystem.exception.FeignServiceUnavailableException;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.feign.KeycloakFeignClient;
import feign.FeignException;
import jakarta.validation.ValidationException;
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

import java.util.List;
import java.util.stream.Stream;

import static com.annakirillova.crmsystem.FeignClientTestData.KEYCLOAK_USER_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.KEYCLOAK_USER_RESPONSE;
import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.PASSWORD_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakFeignClientHelperUnitTest {

    @Mock
    private KeycloakFeignClient keycloakFeignClient;

    @InjectMocks
    private KeycloakFeignClientHelper keycloakFeignClientHelper;

    @Test
    void createUserSuccess() {
        when(keycloakFeignClient.createUser(anyString(), any(KeycloakUserDto.class))).thenReturn(ResponseEntity.ok().build());

        keycloakFeignClientHelper.createUserWithCircuitBreaker(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO);

        verify(keycloakFeignClient, times(1)).createUser(anyString(), any(KeycloakUserDto.class));
    }

    @Test
    void updatePasswordSuccess() {
        when(keycloakFeignClient.updatePassword(anyString(), anyString(), any(CredentialRepresentationDto.class))).thenReturn(ResponseEntity.ok().build());

        keycloakFeignClientHelper.updatePasswordWithCircuitBreaker(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO.getId(), PASSWORD_DTO);

        verify(keycloakFeignClient, times(1)).updatePassword(anyString(), anyString(), any(CredentialRepresentationDto.class));
    }

    @Test
    void deleteUserSuccess() {
        when(keycloakFeignClient.deleteUser(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());

        keycloakFeignClientHelper.deleteUserWithCircuitBreaker(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO.getId());

        verify(keycloakFeignClient, times(1)).deleteUser(anyString(), anyString());
    }

    @Test
    void getUserByUsernameSuccess() {
        when(keycloakFeignClient.getUserByUsername(anyString(), anyString())).thenReturn(KEYCLOAK_USER_RESPONSE);

        ResponseEntity<List<KeycloakUserDto>> response = keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(TOKEN_RESPONSE_DTO.getAccessToken(), LOGIN_REQUEST_DTO.getUsername());

        verify(keycloakFeignClient, times(1)).getUserByUsername(anyString(), anyString());
        assertEquals(KEYCLOAK_USER_RESPONSE, response);
    }

    @Test
    void updateUserSuccess() {
        when(keycloakFeignClient.updateUser(anyString(), anyString(), any(KeycloakUserDto.class))).thenReturn(ResponseEntity.ok().build());

        keycloakFeignClientHelper.updateUserWithCircuitBreaker(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO.getId(), KEYCLOAK_USER_DTO);

        verify(keycloakFeignClient, times(1)).updateUser(anyString(), anyString(), any(KeycloakUserDto.class));
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
    void createUserFallbackFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakFeignClientHelper.createUserFallback(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO, feignException));
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void updatePasswordFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakFeignClientHelper.updatePasswordFallback(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO.getId(), PASSWORD_DTO, feignException));
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void deleteUserFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakFeignClientHelper.deleteUserFallback(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO.getId(), feignException));
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void getUserByUsernameFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakFeignClientHelper.getUserByUsernameFallback(TOKEN_RESPONSE_DTO.getAccessToken(), LOGIN_REQUEST_DTO.getUsername(), feignException));
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void updateUserFeignException(int status, Class<? extends Exception> expectedException) {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(status);

        assertThrows(expectedException, () -> keycloakFeignClientHelper.updateUserFallback(TOKEN_RESPONSE_DTO.getAccessToken(), KEYCLOAK_USER_DTO.getId(), KEYCLOAK_USER_DTO, feignException));
    }
}
