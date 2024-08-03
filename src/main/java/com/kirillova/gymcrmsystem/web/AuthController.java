package com.kirillova.gymcrmsystem.web;

import com.kirillova.gymcrmsystem.dto.AuthResponseDto;
import com.kirillova.gymcrmsystem.dto.LoginRequestDto;
import com.kirillova.gymcrmsystem.security.JWTProvider;
import com.kirillova.gymcrmsystem.service.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final BruteForceProtectionService bruteForceProtectionService;

    @PostMapping("/login")
    public AuthResponseDto authenticate(@RequestBody LoginRequestDto loginRequest) {
        String username = loginRequest.getUsername();
        log.info("Attempt to login by user: {}", username);

        if (bruteForceProtectionService.isBlocked(username)) {
            throw new BadCredentialsException("User is blocked due to multiple failed login attempts. Please try again later.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            bruteForceProtectionService.resetBlock(username);

            String jwt = jwtProvider.createToken(username);
            log.info("Login successful for user: {}", username);

            return new AuthResponseDto(jwt);
        } catch (BadCredentialsException e) {
            bruteForceProtectionService.loginFailed(username);
            throw e;
        }
    }
}
