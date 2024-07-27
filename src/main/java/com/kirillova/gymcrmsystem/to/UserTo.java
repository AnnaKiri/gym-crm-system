package com.kirillova.gymcrmsystem.to;

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
public class UserTo extends BaseTo {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 5, max = 50)
    private String password;

    @NotBlank
    @Size(min = 5, max = 50)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private String newPassword;

    public UserTo(Integer id, String username, String password) {
        super.setId(id);
        this.username = username;
        this.password = password;
    }

}
