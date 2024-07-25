package com.kirillova.gymcrmsystem.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Size(min = 5, max = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    public UserTo(Integer id, String username, String password) {
        super.setId(id);
        this.username = username;
        this.password = password;
    }

}
