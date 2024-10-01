package com.annakirillova.crmsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private List<CredentialRepresentation> credentials;
}
