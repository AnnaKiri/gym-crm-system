package com.annakirillova.crmsystem.web;

import com.annakirillova.crmsystem.dto.AuthResponseDto;
import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.service.BruteForceProtectionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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

    @PostMapping("/login")
    @Transactional
    public AuthResponseDto authenticate(@RequestBody LoginRequestDto loginRequest) {
        String username = loginRequest.getUsername();
        log.info("Attempt to login by user: {}", username);

        if (bruteForceProtectionService.isBlocked(username)) {
            throw new BadCredentialsException("User is blocked due to multiple failed login attempts. Please try again later.");
        }

        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);

            bruteForceProtectionService.resetBlock(username);

//            String jwt = jwtProvider.createToken(username);
            log.info("Login successful for user: {}", username);

            return new AuthResponseDto("jwt");
        } catch (BadCredentialsException e) {
            bruteForceProtectionService.loginFailed(username);
            throw e;
        }
    }

    @PostMapping("/logout")
    @Transactional
    public String logout(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {

//        throw new BadCredentialsException("Invalid token");
        return "";
    }
}
