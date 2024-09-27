package com.annakirillova.trainerworkloadservice.util;

import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class TrainerUtil {

    public static TrainerDto createDtoWithMonthlySummary(Trainer trainer, Map<Integer, Map<String, Integer>> monthlySummary) {

        return TrainerDto.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .monthlySummary(monthlySummary)
                .build();
    }
}
