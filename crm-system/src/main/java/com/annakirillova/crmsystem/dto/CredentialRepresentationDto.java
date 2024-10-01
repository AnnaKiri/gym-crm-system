package com.annakirillova.crmsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CredentialRepresentationDto {
    private String type;
    private String value;
    private boolean temporary;

    public CredentialRepresentationDto(String value) {
        this.type = "password";
        this.value = value;
        this.temporary = false;
    }
}