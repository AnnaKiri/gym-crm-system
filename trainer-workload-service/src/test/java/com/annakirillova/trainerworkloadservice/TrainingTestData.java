package com.annakirillova.trainerworkloadservice;

import com.annakirillova.trainerworkloadservice.dto.TrainingInfoDto;

import java.time.LocalDate;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2;

public class TrainingTestData {

    public static final TrainingInfoDto TRAINING_DTO_ADD = TrainingInfoDto.builder()
            .username(TRAINER_1.getUsername())
            .firstName(TRAINER_1.getFirstName())
            .lastName(TRAINER_1.getLastName())
            .isActive(TRAINER_1.isActive())
            .date(LocalDate.of(2024, 1, 2))
            .duration(60)
            .actionType(TrainingInfoDto.ACTION_TYPE_ADD)
            .build();

    public static final TrainingInfoDto TRAINING_DTO_DELETE = TrainingInfoDto.builder()
            .username(TRAINER_2.getUsername())
            .firstName(TRAINER_2.getFirstName())
            .lastName(TRAINER_2.getLastName())
            .isActive(TRAINER_2.isActive())
            .date(LocalDate.of(2024, 1, 2))
            .duration(60)
            .actionType(TrainingInfoDto.ACTION_TYPE_DELETE)
            .build();

    public static final TrainingInfoDto TRAINING_DTO_INVALID_ACTION_TYPE = TrainingInfoDto.builder()
            .username(TRAINER_1.getUsername())
            .firstName(TRAINER_1.getFirstName())
            .lastName(TRAINER_1.getLastName())
            .isActive(TRAINER_1.isActive())
            .date(LocalDate.of(2024, 1, 2))
            .duration(60)
            .actionType("INVALID_ACTION_TYPE")
            .build();
}
