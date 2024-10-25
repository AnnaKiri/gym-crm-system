package com.annakirillova.e2etests.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(min = 2, max = 128)
    @NotBlank
    private String name;

    private TrainingType type;

    @NotNull
    private Integer typeId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer duration;

    @NotBlank
    private String traineeUsername;

    @NotBlank
    private String trainerUsername;
}
