package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.config.SecurityConfig;
import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

    private final KeycloakFeignClientHelper keycloakFeignClientHelper;
    private final TokenService tokenService;

    public void registerUser(String username, String firstName, String lastName, String password) {
        log.info("Registering user with username: {}", username);

        KeycloakUserDto user = KeycloakUserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .enabled(true)
                .build();
        CredentialRepresentationDto credential = new CredentialRepresentationDto(password);
        user.setCredentials(List.of(credential));

        String adminToken = tokenService.getAdminToken();
        keycloakFeignClientHelper.createUserWithCircuitBreaker(SecurityConfig.BEARER_PREFIX + adminToken, user);
        log.info("User {} registered successfully.", username);
    }

    public void updatePassword(String username, String newPassword) {
        log.info("Updating password for user: {}", username);

        KeycloakUserDto user = getUserByUsername(username);
        CredentialRepresentationDto passwordDto = new CredentialRepresentationDto(newPassword);
        String adminToken = tokenService.getAdminToken();
        keycloakFeignClientHelper.updatePasswordWithCircuitBreaker(SecurityConfig.BEARER_PREFIX + adminToken, user.getId(), passwordDto);
        log.info("Password updated successfully for user: {}", username);
    }

    public void deleteUser(String username) {
        log.info("Deleting user with username: {}", username);

        KeycloakUserDto user = getUserByUsername(username);
        String adminToken = tokenService.getAdminToken();
        keycloakFeignClientHelper.deleteUserWithCircuitBreaker(SecurityConfig.BEARER_PREFIX + adminToken, user.getId());
        log.info("User {} deleted successfully.", username);
    }

    public void updateUser(String username, String newFirstName, String newLastName) {
        log.info("Updating user with username: {}", username);

        KeycloakUserDto user = getUserByUsername(username);
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        String adminToken = tokenService.getAdminToken();

        keycloakFeignClientHelper.updateUserWithCircuitBreaker(SecurityConfig.BEARER_PREFIX + adminToken, user.getId(), user);
        log.info("User {} updated successfully.", username);
    }

    private KeycloakUserDto getUserByUsername(String username) {
        String adminToken = tokenService.getAdminToken();
        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(SecurityConfig.BEARER_PREFIX + adminToken, username);
        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            return userResponse.getBody().getFirst();
        } else {
            throw new NotFoundException("User not found: " + username);
        }
    }
}
