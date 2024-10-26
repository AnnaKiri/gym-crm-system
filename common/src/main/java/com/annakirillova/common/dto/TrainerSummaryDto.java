package com.annakirillova.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrainerSummaryDto {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean isActive;

    @NotNull
    private List<Summary> summaryList;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Summary {

        @NotNull
        @Min(1900)
        @Max(3000)
        private Integer year;

        @NotNull
        @Min(1)
        @Max(12)
        private Integer month;

        @NotNull
        @Positive
        private Integer duration;
    }
}
