package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.error.KeycloakOperationException;
import com.annakirillova.crmsystem.feign.KeycloakFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakFeignClientHelper {

    private final KeycloakFeignClient keycloakFeignClient;

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "createUserFallback")
    @TimeLimiter(name = "keycloakService")
    public CompletableFuture<ResponseEntity<Void>> createUserWithCircuitBreaker(String token, KeycloakUserDto user) {
        return CompletableFuture.supplyAsync(() -> keycloakFeignClient.createUser(token, user));
    }

    public CompletableFuture<ResponseEntity<Void>> createUserFallback(String token, KeycloakUserDto user, Throwable throwable) {
        log.error("Failed to create user: {}. Error: {}", user.getUsername(), throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Failed to create user: " + user.getUsername() + ". Error: " + throwable.getMessage())
        );
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "updatePasswordFallback")
    @TimeLimiter(name = "keycloakService")
    public CompletableFuture<ResponseEntity<Void>> updatePasswordWithCircuitBreaker(String token, String userId, CredentialRepresentationDto credential) {
        return CompletableFuture.supplyAsync(() -> keycloakFeignClient.updatePassword(token, userId, credential));
    }

    public CompletableFuture<ResponseEntity<Void>> updatePasswordFallback(String token, String userId, CredentialRepresentationDto credential, Throwable throwable) {
        log.error("Failed to update password for user ID: {}. Error: {}", userId, throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Failed to update password for user ID: " + userId + ". Error: " + throwable.getMessage())
        );
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "deleteUserFallback")
    @TimeLimiter(name = "keycloakService")
    public CompletableFuture<ResponseEntity<Void>> deleteUserWithCircuitBreaker(String token, String userId) {
        return CompletableFuture.supplyAsync(() -> keycloakFeignClient.deleteUser(token, userId));
    }

    public CompletableFuture<ResponseEntity<Void>> deleteUserFallback(String token, String userId, Throwable throwable) {
        log.error("Failed to delete user ID: {}. Error: {}", userId, throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Failed to delete user ID: " + userId + ". Error: " + throwable.getMessage())
        );
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "getUserByUsernameFallback")
    @TimeLimiter(name = "keycloakService")
    public CompletableFuture<ResponseEntity<List<KeycloakUserDto>>> getUserByUsernameWithCircuitBreaker(String token, String username) {
        return CompletableFuture.supplyAsync(() -> keycloakFeignClient.getUserByUsername(token, username));
    }

    public CompletableFuture<ResponseEntity<List<KeycloakUserDto>>> getUserByUsernameFallback(String token, String username, Throwable throwable) {
        log.error("Failed to retrieve user by username: {}. Error: {}", username, throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Failed to retrieve user by username: " + username + ". Error: " + throwable.getMessage())
        );
    }

    @CircuitBreaker(name = "keycloakService", fallbackMethod = "updateUserFallback")
    @TimeLimiter(name = "keycloakService")
    public CompletableFuture<ResponseEntity<Void>> updateUserWithCircuitBreaker(String token, String userId, KeycloakUserDto user) {
        return CompletableFuture.supplyAsync(() -> keycloakFeignClient.updateUser(token, userId, user));
    }

    public CompletableFuture<ResponseEntity<Void>> updateUserFallback(String token, String userId, KeycloakUserDto user, Throwable throwable) {
        log.error("Failed to update user ID: {}. Error: {}", userId, throwable.getMessage());
        return CompletableFuture.failedFuture(
                new KeycloakOperationException("Failed to update user ID: " + userId + ". Error: " + throwable.getMessage())
        );
    }
}
