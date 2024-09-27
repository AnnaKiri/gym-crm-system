package com.annakirillova.trainerworkloadservice.web;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    public AuthUser(String username) {
        super(username, "", Collections.emptyList());
    }

    public static Optional<AuthUser> safeGet() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> (AuthUser) authentication.getPrincipal());
    }

    public static AuthUser get() {
        return safeGet().orElseThrow(() -> new RuntimeException("No authorized user found"));
    }

    @Override
    public String toString() {
        return "AuthUser[" + getUsername() + "]";
    }
}
