package com.annakirillova.crmsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.annakirillova.crmsystem.models.TrainingType;
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

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TrainingDto extends BaseDto {

    @Size(min = 2, max = 128)
    @NotBlank
    private String name;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private TrainingType type;

    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
