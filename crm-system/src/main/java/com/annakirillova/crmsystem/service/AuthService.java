package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.feign.KeycloakFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final KeycloakFeignClient keycloakClient;
    private final TokenService tokenService;

    public String getJwtToken() {
        return "";
    }

    @Autowired
    public AuthService(KeycloakFeignClient keycloakClient, TokenService tokenService) {
        this.keycloakClient = keycloakClient;
        this.tokenService = tokenService;
    }

    public void registerUser(String username, String firstName, String lastName, String password) {
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
    }

    public void updatePassword(String username, String newPassword) {
        String adminToken = tokenService.getAdminToken().getAccessToken();

        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakClient.getUserByUsername("Bearer " + adminToken, username);

        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            KeycloakUserDto user = userResponse.getBody().getFirst();
            String userId = user.getId();

            CredentialRepresentationDto passwordDto = new CredentialRepresentationDto(newPassword);

            ResponseEntity<Void> response = keycloakClient.updatePassword("Bearer " + adminToken, userId, passwordDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Password updated successfully for user: " + username);
            } else {
                throw new RuntimeException("Failed to update password for user: " + username);
            }
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public void deleteUser(String username) {
        String adminToken = tokenService.getAdminToken().getAccessToken();

        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakClient.getUserByUsername("Bearer " + adminToken, username);

        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            KeycloakUserDto user = userResponse.getBody().getFirst();
            String userId = user.getId();

            ResponseEntity<Void> response = keycloakClient.deleteUser("Bearer " + adminToken, userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("User deleted successfully: " + username);
            } else {
                throw new RuntimeException("Failed to delete user: " + username);
            }
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public void updateUser(String username, String newFirstName, String newLastName) {
        String adminToken = tokenService.getAdminToken().getAccessToken();

        ResponseEntity<List<KeycloakUserDto>> userResponse = keycloakClient.getUserByUsername("Bearer " + adminToken, username);

        if (userResponse.getBody() != null && !userResponse.getBody().isEmpty()) {
            KeycloakUserDto user = userResponse.getBody().getFirst();
            String userId = user.getId();

            user.setFirstName(newFirstName);
            user.setLastName(newLastName);

            ResponseEntity<Void> response = keycloakClient.updateUser("Bearer " + adminToken, userId, user);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("User updated successfully: " + username);
            } else {
                throw new RuntimeException("Failed to update user: " + username);
            }
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }
}
