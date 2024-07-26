package com.kirillova.gymcrmsystem.to;

import com.kirillova.gymcrmsystem.models.TrainingType;
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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TrainingTo extends BaseTo {

    @Size(min = 2, max = 128)
    @NotBlank
    private String name;

    @NotNull
    private TrainingType type;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer duration;

    @NotBlank
    private String traineeName;

    @NotBlank
    private String trainerName;
}
