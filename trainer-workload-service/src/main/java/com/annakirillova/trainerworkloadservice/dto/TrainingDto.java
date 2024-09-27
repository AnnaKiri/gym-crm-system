package com.annakirillova.trainerworkloadservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TrainingDto {

    public static final String ACTION_TYPE_ADD = "ADD";
    public static final String ACTION_TYPE_DELETE = "DELETE";

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private boolean isActive;

    @NotNull
    private LocalDate date;

    @NotNull
    private int duration;

    @NotBlank
    private String actionType;
}
