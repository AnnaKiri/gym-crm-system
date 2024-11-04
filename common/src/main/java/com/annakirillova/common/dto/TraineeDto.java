package com.annakirillova.common.dto;

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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class TraineeDto extends BaseDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;

    private LocalDate birthday;

    private String address;

    @NotNull
    private Boolean isActive;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<TrainerDto> trainerList;

    public TraineeDto(TraineeDto other) {
        super.id = other.getId();
        this.username = other.username;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.birthday = other.birthday;
        this.address = other.address;
        this.isActive = other.isActive;
        this.trainerList = new ArrayList<>();
    }
}
