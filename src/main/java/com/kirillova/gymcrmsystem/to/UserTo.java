package com.kirillova.gymcrmsystem.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class UserTo extends BaseTo {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 5, max = 50)
    private String password;

    @NotBlank
    @Size(min = 5, max = 50)
    private String newPassword;

    public UserTo(Integer id, String username, String password) {
        super.setId(id);
        this.username = username;
        this.password = password;
    }

}
