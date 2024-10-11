package com.annakirillova.trainerworkloadservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDto {

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotNull
    private Integer duration;
}
