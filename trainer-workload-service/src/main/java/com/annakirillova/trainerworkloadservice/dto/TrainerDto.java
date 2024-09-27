package com.annakirillova.trainerworkloadservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
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
    private List<SummaryDto> summaryList;
}
