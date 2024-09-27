package com.annakirillova.trainerworkloadservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SummaryDto {

    @NotNull
    private int year;

    @NotNull
    private int month;

    @NotNull
    private int duration;

    public SummaryDto(int year, int month, int duration) {
        this.year = year;
        this.month = month;
        this.duration = duration;
    }
}
