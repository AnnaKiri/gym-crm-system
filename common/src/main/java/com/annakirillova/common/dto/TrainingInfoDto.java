package com.annakirillova.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingInfoDto {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean isActive;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer duration;

    @NotNull
    private ActionType actionType;
}
