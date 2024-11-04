package com.annakirillova.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CredentialRepresentationDto {

    public static final String PASSWORD_STRING = "password";

    private String type;
    private String value;
    private boolean temporary;

    public CredentialRepresentationDto(String value) {
        this.type = PASSWORD_STRING;
        this.value = value;
        this.temporary = false;
    }
}