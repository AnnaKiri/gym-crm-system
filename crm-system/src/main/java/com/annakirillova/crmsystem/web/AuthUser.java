package com.annakirillova.crmsystem.web;

import com.annakirillova.crmsystem.error.NotFoundException;
import com.annakirillova.crmsystem.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final User user;

    @Getter
    @Setter
    private String token;

    public AuthUser(@NonNull User user) {
        super(user.getUsername(), user.getPassword(), Collections.EMPTY_LIST);
        this.user = user;
    }

    public static Optional<AuthUser> safeGet() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(AuthUser.class::isInstance)
                .map(AuthUser.class::cast);
    }

    public static AuthUser get() {
        return safeGet().orElseThrow(() -> new NotFoundException("No authorized user found"));
    }

    public static int authId() {
        return get().id();
    }

    public int id() {
        return user.id();
    }

    public static String getJwtToken() {
        return get().getToken();
    }

    @Override
    public String toString() {
        return "AuthUser:" + user.getId() + '[' + user.getUsername() + ']';
    }
}
