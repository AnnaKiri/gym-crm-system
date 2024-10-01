package com.annakirillova.crmsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CredentialRepresentation {
    private String type;
    private String value;
    private boolean temporary;

    public CredentialRepresentation(String value) {
        this.type = "password";
        this.value = value;
        this.temporary = false;
    }
}