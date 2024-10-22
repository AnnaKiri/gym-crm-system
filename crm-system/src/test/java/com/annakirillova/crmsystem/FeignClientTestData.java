package com.annakirillova.crmsystem;

import com.annakirillova.crmsystem.config.KeycloakProperties;
import com.annakirillova.crmsystem.dto.CredentialRepresentationDto;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

public class FeignClientTestData {
    public static final TokenResponseDto TOKEN_RESPONSE_DTO = new TokenResponseDto("access-token", 5L, "refresh-token");
    public static final LoginRequestDto LOGIN_REQUEST_DTO = new LoginRequestDto("user1", "password1");
    public static final KeycloakUserDto KEYCLOAK_USER_DTO = KeycloakUserDto.builder().id("id").build();
    public static final ResponseEntity<List<KeycloakUserDto>> KEYCLOAK_USER_RESPONSE = ResponseEntity.ok(List.of(KEYCLOAK_USER_DTO));
    public static final MatcherFactory.Matcher<TokenResponseDto> TOKEN_RESPONSE_DTO_MATCHER = MatcherFactory.usingEqualsComparator(TokenResponseDto.class);
    public static final KeycloakProperties.User USER_PROPERTIES = new KeycloakProperties.User();
    public static final KeycloakProperties.Admin ADMIN_PROPERTIES = new KeycloakProperties.Admin();
    public static final CredentialRepresentationDto PASSWORD_DTO = new CredentialRepresentationDto("newPassword");

    public static final Jwt JWT_DUMB = Jwt.withTokenValue("access-token")
            .header("alg", "none")
            .claim("exp", Instant.now().plusSeconds(3600))
            .build();

    static {
        USER_PROPERTIES.setClientId("user-client");
        USER_PROPERTIES.setClientSecret("user-secret");

        ADMIN_PROPERTIES.setClientId("admin-client");
        ADMIN_PROPERTIES.setClientSecret("admin-secret");
        ADMIN_PROPERTIES.setUsername("admin");
        ADMIN_PROPERTIES.setPassword("admin-password");
    }

}
