package com.kirillova.gymcrmsystem.to;

import com.kirillova.gymcrmsystem.models.TrainingType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class TrainerTo extends BaseTo {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;

    @NotBlank
    private boolean isActive;

    @ToString.Exclude
    private List<TraineeTo> traineeList;

    @NotNull
    private TrainingType specialization;
}
