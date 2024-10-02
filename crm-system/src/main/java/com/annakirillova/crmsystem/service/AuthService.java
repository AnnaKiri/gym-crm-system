package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.feign.KeycloakFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KeycloakFeignClient keycloakClient;
    private final TokenService tokenService;

    public String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            log.debug("Retrieved JWT token for the current authentication context.");

            return jwtToken.getToken().getTokenValue();
        }

        log.warn("Failed to retrieve JWT token: Authentication is not of type JwtAuthenticationToken.");

        return null;
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String username = jwtToken.getToken().getClaimAsString("preferred_username");

            log.debug("Retrieved username: {}", username);

            return username;
        }
        log.warn("Failed to retrieve username: Authentication is not of type JwtAuthenticationToken.");

        return null;
    }

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

        String adminToken = tokenService.getAdminToken().getAccessToken();
        keycloakClient.createUser("Bearer " + adminToken, user);

        log.info("User {} registered successfully.", username);
    }

    public void updatePassword(String username, String newPassword) {
        log.info("Updating password for user: {}", username);

        String adminToken = tokenService.getAdminToken().getAccessToken();

        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakClient.getUserByUsername("Bearer " + adminToken, username);

        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            KeycloakUserDto user = userResponse.getBody().getFirst();
            String userId = user.getId();

            CredentialRepresentationDto passwordDto = new CredentialRepresentationDto(newPassword);

            ResponseEntity<Void> response = keycloakClient.updatePassword("Bearer " + adminToken, userId, passwordDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Password updated successfully for user: {}", username);
            } else {
                throw new RuntimeException("Failed to update password for user: " + username);
            }
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public void deleteUser(String username) {
        log.info("Deleting user with username: {}", username);

        String adminToken = tokenService.getAdminToken().getAccessToken();

        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakClient.getUserByUsername("Bearer " + adminToken, username);

        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            KeycloakUserDto user = userResponse.getBody().getFirst();
            String userId = user.getId();

            ResponseEntity<Void> response = keycloakClient.deleteUser("Bearer " + adminToken, userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("User {} deleted successfully.", username);
            } else {
                throw new RuntimeException("Failed to delete user: " + username);
            }
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public void updateUser(String username, String newFirstName, String newLastName) {
        log.info("Updating user with username: {}", username);

        String adminToken = tokenService.getAdminToken().getAccessToken();

        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakClient.getUserByUsername("Bearer " + adminToken, username);

        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            KeycloakUserDto user = userResponse.getBody().getFirst();
            String userId = user.getId();

            user.setFirstName(newFirstName);
            user.setLastName(newLastName);

            ResponseEntity<Void> response = keycloakClient.updateUser("Bearer " + adminToken, userId, user);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("User {} updated successfully.", username);
            } else {
                throw new RuntimeException("Failed to update user: " + username);
            }
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }
}
