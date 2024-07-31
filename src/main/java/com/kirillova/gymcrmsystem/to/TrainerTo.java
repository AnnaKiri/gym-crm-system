package com.kirillova.gymcrmsystem.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kirillova.gymcrmsystem.models.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TrainerTo extends BaseTo {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;

    @NotNull
    private Boolean isActive;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<TraineeTo> traineeList;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private TrainingType specialization;

    @NotNull
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer specializationId;
}
