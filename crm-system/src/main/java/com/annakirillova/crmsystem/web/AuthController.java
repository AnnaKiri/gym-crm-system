package com.annakirillova.crmsystem.web;

import com.annakirillova.crmsystem.config.SecurityConfig;
import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.service.BruteForceProtectionService;
import com.annakirillova.crmsystem.service.TokenService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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

    private final BruteForceProtectionService bruteForceProtectionService;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Transactional
    public TokenResponseDto authenticate(@RequestBody LoginRequestDto loginRequest) {
        String username = loginRequest.getUsername();
        log.info("Attempt to login by user: {}", username);

        if (bruteForceProtectionService.isBlocked(username)) {
            throw new BadCredentialsException("User is blocked due to multiple failed login attempts. Please try again later.");
        }

        try {
            TokenResponseDto tokenResponseDto = tokenService.getUserToken(username, loginRequest.getPassword());
            bruteForceProtectionService.resetBlock(username);
            log.info("Login successful for user: {}", username);

            return tokenResponseDto;
        } catch (Exception e) {
            bruteForceProtectionService.loginFailed(username);
            log.info("Wrong credentials for user: {}", username);
            throw new BadCredentialsException("Wrong credentials");
        }
    }

    @PostMapping("/logout")
    public String logout(@Parameter(hidden = true) @RequestHeader("Authorization") String token,
                         @RequestBody TokenResponseDto tokenResponseDto) {
        if (token.startsWith(SecurityConfig.BEARER_PREFIX)) {
            token = token.substring(7);
            tokenService.invalidateToken(token);
            SecurityContextHolder.clearContext();

            try {
                tokenService.logoutUser(tokenResponseDto.getRefreshToken());
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid refresh token");
            }

        } else {
            throw new BadCredentialsException("Invalid access token");
        }

        return "Logged out successfully";
    }
}
