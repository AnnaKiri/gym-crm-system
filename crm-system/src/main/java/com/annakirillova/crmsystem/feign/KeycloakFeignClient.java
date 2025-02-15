package com.annakirillova.crmsystem.feign;

import com.annakirillova.common.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
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

    String USER_ID = "userId";

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @RequestBody KeycloakUserDto user);

    @PutMapping("/users/{userId}/reset-password")
    ResponseEntity<Void> updatePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @PathVariable(USER_ID) String userId,
                                        @RequestBody CredentialRepresentationDto credential
    );

    @GetMapping("/users")
    ResponseEntity<List<KeycloakUserDto>> getUserByUsername(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                            @RequestParam("username") String username);

    @DeleteMapping("/users/{userId}")
    ResponseEntity<Void> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @PathVariable(USER_ID) String userId);

    @PutMapping("/users/{userId}")
    ResponseEntity<Void> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @PathVariable(USER_ID) String userId,
                                    @RequestBody KeycloakUserDto user);

}
