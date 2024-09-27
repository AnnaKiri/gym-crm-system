package com.annakirillova.trainerworkloadservice.util;

import com.annakirillova.trainerworkloadservice.dto.SummaryDto;
import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TrainerUtil {

    public static TrainerDto createDtoWithMonthlySummary(Trainer trainer, List<SummaryDto> summaryDtoList) {

        return TrainerDto.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .summaryList(summaryDtoList)
                .build();
    }
}
