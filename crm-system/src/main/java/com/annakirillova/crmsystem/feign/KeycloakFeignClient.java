package com.annakirillova.crmsystem.feign;

import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "keycloak-service", url = "${keycloak.url}/admin/realms/${keycloak.realm}")
public interface KeycloakFeignClient {

    @PostMapping("/users")
    @CircuitBreaker(name = "keycloak")
    ResponseEntity<Void> createUser(@RequestHeader("Authorization") String token,
                                    @RequestBody KeycloakUserDto user);

    @PutMapping("/users/{userId}/reset-password")
    @CircuitBreaker(name = "keycloak")
    ResponseEntity<Void> updatePassword(@RequestHeader("Authorization") String token,
                                        @PathVariable("userId") String userId,
                                        @RequestBody CredentialRepresentationDto credential
    );

    @GetMapping("/users")
    @CircuitBreaker(name = "keycloak")
    ResponseEntity<List<KeycloakUserDto>> getUserByUsername(@RequestHeader("Authorization") String token,
                                                            @RequestParam("username") String username);

    @DeleteMapping("/users/{userId}")
    @CircuitBreaker(name = "keycloak")
    ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token,
                                    @PathVariable("userId") String userId);

    @PutMapping("/users/{userId}")
    @CircuitBreaker(name = "keycloak")
    ResponseEntity<Void> updateUser(@RequestHeader("Authorization") String token,
                                    @PathVariable("userId") String userId,
                                    @RequestBody KeycloakUserDto user);

}
