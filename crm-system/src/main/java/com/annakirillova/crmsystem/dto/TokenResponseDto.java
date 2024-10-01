package com.annakirillova.crmsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    @JsonProperty(value = "access_token", access = JsonProperty.Access.READ_ONLY)
    private String accessToken;

    @JsonProperty(value = "expires_in", access = JsonProperty.Access.READ_ONLY)
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
