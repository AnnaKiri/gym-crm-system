package com.annakirillova.trainerworkloadservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TrainerDto {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private boolean isActive;

    @NotNull
    private Map<Integer, Map<String, Integer>> monthlySummary;
}
