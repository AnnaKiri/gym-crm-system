package com.kirillova.gymcrmsystem.to;

import com.kirillova.gymcrmsystem.models.TrainingType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
