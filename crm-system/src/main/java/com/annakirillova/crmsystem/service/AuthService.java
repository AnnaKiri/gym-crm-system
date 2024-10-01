package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.CredentialRepresentation;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.feign.KeycloakFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    public String getJwtToken() {
        return "";
    }

    private final KeycloakFeignClient keycloakClient;
    private final TokenService tokenService;

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
        CredentialRepresentation credential = new CredentialRepresentation(password);
        user.setCredentials(List.of(credential));

        String adminToken = tokenService.getAdminToken().getAccessToken();
        keycloakClient.createUser("Bearer " + adminToken, user);
    }
}
