package com.kirillova.gymcrmsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class UserDto extends BaseDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @NotBlank
    @Size(min = 5, max = 50)
    private String password;

    @NotBlank
    @Size(min = 5, max = 50)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    public UserDto(Integer id, String username, String password) {
        super.setId(id);
        this.username = username;
        this.password = password;
    }

}
