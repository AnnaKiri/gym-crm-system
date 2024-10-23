package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.config.SecurityConfig;
import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.annakirillova.crmsystem.FeignClientTestData.KEYCLOAK_USER_DTO;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceUnitTest {

    @Mock
    private KeycloakFeignClientHelper keycloakFeignClientHelper;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private KeycloakService keycloakService;

    private String adminToken = "admin-token";

    @BeforeEach
    void setup() {
        when(tokenService.getAdminToken()).thenReturn(adminToken);
    }

    @Test
    void registerUserSuccess() {
        doNothing().when(keycloakFeignClientHelper).createUserWithCircuitBreaker(eq(SecurityConfig.BEARER_PREFIX + adminToken), any(KeycloakUserDto.class));

        keycloakService.registerUser(USER_1.getUsername(), USER_1.getFirstName(), USER_1.getLastName(), "password123");

        verify(keycloakFeignClientHelper, times(1))
                .createUserWithCircuitBreaker(eq(SecurityConfig.BEARER_PREFIX + adminToken), any(KeycloakUserDto.class));
        verify(tokenService, times(1)).getAdminToken();
    }

    @Test
    void updatePasswordSuccess() {
        when(keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(any(String.class), eq(USER_1_USERNAME)))
                .thenReturn(ResponseEntity.ok(List.of(KEYCLOAK_USER_DTO)));

        doNothing().when(keycloakFeignClientHelper).updatePasswordWithCircuitBreaker(any(String.class), eq(KEYCLOAK_USER_DTO.getId()), any(CredentialRepresentationDto.class));

        keycloakService.updatePassword(USER_1_USERNAME, "newPassword");

        verify(keycloakFeignClientHelper, times(1))
                .updatePasswordWithCircuitBreaker(eq(SecurityConfig.BEARER_PREFIX + adminToken), eq(KEYCLOAK_USER_DTO.getId()), any(CredentialRepresentationDto.class));
    }

    @Test
    void deleteUserSuccess() {
        when(keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(any(String.class), eq(USER_1_USERNAME)))
                .thenReturn(ResponseEntity.ok(List.of(KEYCLOAK_USER_DTO)));

        doNothing().when(keycloakFeignClientHelper).deleteUserWithCircuitBreaker(any(String.class), eq(KEYCLOAK_USER_DTO.getId()));

        keycloakService.deleteUser(USER_1_USERNAME);

        verify(keycloakFeignClientHelper, times(1))
                .deleteUserWithCircuitBreaker(eq(SecurityConfig.BEARER_PREFIX + adminToken), eq(KEYCLOAK_USER_DTO.getId()));
    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {
        when(keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(any(String.class), eq(USER_1_USERNAME)))
                .thenReturn(ResponseEntity.ok(List.of(KEYCLOAK_USER_DTO)));

        doNothing().when(keycloakFeignClientHelper).updateUserWithCircuitBreaker(any(String.class), eq(KEYCLOAK_USER_DTO.getId()), any(KeycloakUserDto.class));

        keycloakService.updateUser(USER_1.getUsername(), USER_2.getFirstName(), USER_2.getLastName());

        verify(keycloakFeignClientHelper, times(1))
                .updateUserWithCircuitBreaker(eq(SecurityConfig.BEARER_PREFIX + adminToken), eq(KEYCLOAK_USER_DTO.getId()), any(KeycloakUserDto.class));
    }
}
