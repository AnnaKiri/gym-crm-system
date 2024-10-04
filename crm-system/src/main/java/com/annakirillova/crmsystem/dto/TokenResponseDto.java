package com.annakirillova.crmsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenResponseDto {

    @JsonProperty("access_token")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String accessToken;

    @JsonProperty("expires_in")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
