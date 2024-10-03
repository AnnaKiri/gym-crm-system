package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
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

        try {
            String adminToken = tokenService.getAdminToken();
            keycloakFeignClientHelper.createUserWithCircuitBreaker("Bearer " + adminToken, user).get();
            log.info("User {} registered successfully.", username);
        } catch (Exception e) {
            throw new KeycloakOperationException("Failed to register user {} : " + username);
        }
    }

    public void updatePassword(String username, String newPassword) {
        log.info("Updating password for user: {}", username);

        KeycloakUserDto user = getUserByUsername(username);
        if (user != null) {
            CredentialRepresentationDto passwordDto = new CredentialRepresentationDto(newPassword);
            try {
                String adminToken = tokenService.getAdminToken();
                keycloakFeignClientHelper.updatePasswordWithCircuitBreaker("Bearer " + adminToken, user.getId(), passwordDto).get();
                log.info("Password updated successfully for user: {}", username);
            } catch (Exception e) {
                throw new KeycloakOperationException("Failed to update password for user: " + username);
            }
        } else {
            throw new NotFoundException("User not found: " + username);
        }
    }

    public void deleteUser(String username) {
        log.info("Deleting user with username: {}", username);

        KeycloakUserDto user = getUserByUsername(username);
        if (user != null) {
            try {
                String adminToken = tokenService.getAdminToken();
                keycloakFeignClientHelper.deleteUserWithCircuitBreaker("Bearer " + adminToken, user.getId()).get();
                log.info("User {} deleted successfully.", username);
            } catch (Exception e) {
                throw new KeycloakOperationException("Failed to delete user: " + username);
            }
        } else {
            throw new NotFoundException("User not found: " + username);
        }
    }

    public void updateUser(String username, String newFirstName, String newLastName) {
        log.info("Updating user with username: {}", username);

        KeycloakUserDto user = getUserByUsername(username);
        if (user != null) {
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            String adminToken = tokenService.getAdminToken();

            try {
                keycloakFeignClientHelper.updateUserWithCircuitBreaker("Bearer " + adminToken, user.getId(), user).get();
                log.info("User {} updated successfully.", username);
            } catch (Exception e) {
                throw new KeycloakOperationException("Failed to update user: " + username);
            }
        } else {
            throw new NotFoundException("User not found: " + username);
        }
    }

    private KeycloakUserDto getUserByUsername(String username) {
        String adminToken = tokenService.getAdminToken();
        try {
            ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker("Bearer " + adminToken, username).get();
            if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
                return userResponse.getBody().getFirst();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new KeycloakOperationException("Failed to retrieve user by username: " + username);
        }
    }
}
