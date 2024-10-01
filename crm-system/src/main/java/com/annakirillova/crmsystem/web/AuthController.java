package com.annakirillova.crmsystem.web;

import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import com.annakirillova.crmsystem.service.BruteForceProtectionService;
import com.annakirillova.crmsystem.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            TokenResponseDto tokenResponseDto = tokenService.getOrdinaryToken(username, loginRequest.getPassword());
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
    @Transactional
    public String logout(@RequestBody TokenResponseDto tokenResponseDto) {
        try {
            tokenService.logoutUser(tokenResponseDto.getRefreshToken());
            return "Logged out successfully";
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token");
        }
    }
}
