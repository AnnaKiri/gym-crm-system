package com.kirillova.gymcrmsystem.web;

import com.kirillova.gymcrmsystem.dto.AuthResponseDto;
import com.kirillova.gymcrmsystem.dto.LoginRequestDto;
import com.kirillova.gymcrmsystem.security.JWTProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JWTProvider jwtProvider;

    @PostMapping("/login")
    public AuthResponseDto authenticate(@RequestBody LoginRequestDto loginRequest) {
        log.info("Attempt to login by user: {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.createToken(loginRequest.getUsername());
        log.info("Login successful for user: {}", loginRequest.getUsername());
        return new AuthResponseDto(jwt);

    }
}
