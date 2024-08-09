package com.kirillova.gymcrmsystem.web;

import com.kirillova.gymcrmsystem.dto.AuthResponseDto;
import com.kirillova.gymcrmsystem.dto.LoginRequestDto;
import com.kirillova.gymcrmsystem.security.JWTProvider;
import com.kirillova.gymcrmsystem.service.BruteForceProtectionService;
import com.kirillova.gymcrmsystem.service.TokenService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final BruteForceProtectionService bruteForceProtectionService;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Transactional
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

    @PostMapping("/logout")
    @Transactional
    public String logout(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenService.invalidateToken(token, jwtProvider.getExpirationTimeSeconds(token));
            SecurityContextHolder.clearContext();
            return "Logged out successfully";
        }
        throw new BadCredentialsException("Invalid token");
    }
}
