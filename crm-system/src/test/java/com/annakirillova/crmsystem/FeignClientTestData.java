package com.annakirillova.crmsystem;

import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.dto.LoginRequestDto;
import com.annakirillova.crmsystem.dto.TokenResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class FeignClientTestData {
    public static final TokenResponseDto TOKEN_RESPONSE_DTO = new TokenResponseDto("access-token", 5L, "refresh-token");
    public static final LoginRequestDto LOGIN_REQUEST_DTO = new LoginRequestDto("user1", "password1");
    public static final KeycloakUserDto KEYCLOAK_USER_DTO = KeycloakUserDto.builder().id("id").build();
    public static final ResponseEntity<List<KeycloakUserDto>> KEYCLOAK_USER_RESPONSE = ResponseEntity.ok(List.of(KEYCLOAK_USER_DTO));
    public static final MatcherFactory.Matcher<TokenResponseDto> TOKEN_RESPONSE_DTO_MATCHER = MatcherFactory.usingEqualsComparator(TokenResponseDto.class);

}
