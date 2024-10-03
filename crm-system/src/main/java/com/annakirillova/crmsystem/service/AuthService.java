package com.annakirillova.crmsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

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
}
