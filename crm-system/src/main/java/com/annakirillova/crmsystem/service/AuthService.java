package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
import com.annakirillova.crmsystem.error.NotFoundException;
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

    private final KeycloakFeignClientHelper keycloakFeignClientHelper;
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
        try {
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
                keycloakFeignClientHelper.updatePasswordWithCircuitBreaker("Bearer " + getAdminToken(), user.getId(), passwordDto).get();
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
                keycloakFeignClientHelper.deleteUserWithCircuitBreaker("Bearer " + getAdminToken(), user.getId()).get();
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

            try {
                keycloakFeignClientHelper.updateUserWithCircuitBreaker("Bearer " + getAdminToken(), user.getId(), user).get();
                log.info("User {} updated successfully.", username);
            } catch (Exception e) {
                throw new KeycloakOperationException("Failed to update user: " + username);
            }
        } else {
            throw new NotFoundException("User not found: " + username);
        }
    }

    private String getAdminToken() {
        return tokenService.getAdminToken().getAccessToken();
    }

    private KeycloakUserDto getUserByUsername(String username) {
        String adminToken = getAdminToken();
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
