package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
import com.annakirillova.crmsystem.feign.KeycloakFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakFeignClientHelper {

    private final KeycloakFeignClient keycloakFeignClient;

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "createUserFallback")
    public void createUserWithCircuitBreaker(String token, KeycloakUserDto user) {
        keycloakFeignClient.createUser(token, user);
    }

    public void createUserFallback(String token, KeycloakUserDto user, Throwable throwable) {
        log.error("Failed to create user: {}. Error: {}", user.getUsername(), throwable.getMessage());
        throw new KeycloakOperationException("Failed to create user: " + user.getUsername() + ". Error: " + throwable.getMessage());
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "updatePasswordFallback")
    public void updatePasswordWithCircuitBreaker(String token, String userId, CredentialRepresentationDto credential) {
        keycloakFeignClient.updatePassword(token, userId, credential);
    }

    public void updatePasswordFallback(String token, String userId, CredentialRepresentationDto credential, Throwable throwable) {
        log.error("Failed to update password for user ID: {}. Error: {}", userId, throwable.getMessage());
        throw new KeycloakOperationException("Failed to update password for user ID: " + userId + ". Error: " + throwable.getMessage());
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "deleteUserFallback")
    public void deleteUserWithCircuitBreaker(String token, String userId) {
        keycloakFeignClient.deleteUser(token, userId);
    }

    public void deleteUserFallback(String token, String userId, Throwable throwable) {
        log.error("Failed to delete user ID: {}. Error: {}", userId, throwable.getMessage());
        throw new KeycloakOperationException("Failed to delete user ID: " + userId + ". Error: " + throwable.getMessage());
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "getUserByUsernameFallback")
    public ResponseEntity<List<KeycloakUserDto>> getUserByUsernameWithCircuitBreaker(String token, String username) {
        return keycloakFeignClient.getUserByUsername(token, username);
    }

    public ResponseEntity<List<KeycloakUserDto>> getUserByUsernameFallback(String token, String username, Throwable throwable) {
        log.error("Failed to retrieve user by username: {}. Error: {}", username, throwable.getMessage());
        throw new KeycloakOperationException("Failed to retrieve user by username: " + username + ". Error: " + throwable.getMessage());
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "updateUserFallback")
    public void updateUserWithCircuitBreaker(String token, String userId, KeycloakUserDto user) {
        keycloakFeignClient.updateUser(token, userId, user);
    }

    public void updateUserFallback(String token, String userId, KeycloakUserDto user, Throwable throwable) {
        log.error("Failed to update user ID: {}. Error: {}", userId, throwable.getMessage());
        throw new KeycloakOperationException("Failed to update user ID: " + userId + ". Error: " + throwable.getMessage());
    }
}
