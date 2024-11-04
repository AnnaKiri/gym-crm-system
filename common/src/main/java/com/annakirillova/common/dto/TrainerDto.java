package com.annakirillova.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TrainerDto extends BaseDto {

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
    private List<TraineeDto> traineeList;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private TrainingTypeDto specialization;

    @NotNull
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer specializationId;

    public TrainerDto(TrainerDto other) {
        super.id = other.getId();
        this.username = other.username;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.isActive = other.isActive;
        this.specialization = other.specialization;
        this.traineeList = new ArrayList<>();
    }
}
