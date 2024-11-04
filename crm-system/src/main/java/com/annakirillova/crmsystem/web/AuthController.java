package com.annakirillova.crmsystem.web;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.crmsystem.service.AuthService;
import com.annakirillova.crmsystem.service.BruteForceProtectionService;
import com.annakirillova.crmsystem.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(AuthController.REST_URL)
@Tag(name = "Auth Controller", description = "Controller for login and logout")
public class AuthController {
    public static final String REST_URL = "/auth";
    public static final String BLOCK_MESSAGE = "User is blocked due to multiple failed login attempts. Please try again later.";

    private final BruteForceProtectionService bruteForceProtectionService;
    private final TokenService tokenService;
    private final AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Operation(summary = "Login", description = "Use this endpoint for getting tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "401", description = "Wrong credentials or you're blocked"),
    })
    public TokenResponseDto authenticate(@RequestBody LoginRequestDto loginRequest) {
        String username = loginRequest.getUsername();
        log.info("Attempt to login by user: {}", username);

        if (bruteForceProtectionService.isBlocked(username)) {
            throw new BadCredentialsException(BLOCK_MESSAGE);
        }

        try {
            TokenResponseDto tokenResponseDto = tokenService.getUserToken(username, loginRequest.getPassword());
            bruteForceProtectionService.resetBlock(username);
            log.info("Login successful for user: {}", username);

            return tokenResponseDto;
        } catch (Exception e) {
            bruteForceProtectionService.loginFailed(username);
            throw e;
        }
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Logout", description = "Use this endpoint for invalidation tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successfully"),
            @ApiResponse(responseCode = "401", description = "Wrong token"),
    })
    public String logout(@RequestBody TokenResponseDto tokenResponseDto) {
        tokenService.invalidateToken(authService.getJwtToken());
        SecurityContextHolder.clearContext();

        tokenService.logoutUser(tokenResponseDto.getRefreshToken());

        return "Logged out successfully";
    }
}
