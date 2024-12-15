package com.annakirillova.trainerworkloadservice;

import com.annakirillova.common.dto.ActionType;
import com.annakirillova.common.dto.TrainingInfoDto;

import java.time.LocalDate;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_1;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;

public class TrainingTestData {

    public static final TrainingInfoDto TRAINING_DTO_ADD = TrainingInfoDto.builder()
            .username(TRAINER_SUMMARY_1.getUsername())
            .firstName(TRAINER_SUMMARY_1.getFirstName())
            .lastName(TRAINER_SUMMARY_1.getLastName())
            .isActive(Boolean.parseBoolean(TRAINER_SUMMARY_1.getIsActive()))
            .date(LocalDate.of(2024, 1, 2))
            .duration(60)
            .actionType(ActionType.ADD)
            .build();

    public static final TrainingInfoDto TRAINING_DTO_DELETE = TrainingInfoDto.builder()
            .username(TRAINER_SUMMARY_2.getUsername())
            .firstName(TRAINER_SUMMARY_2.getFirstName())
            .lastName(TRAINER_SUMMARY_2.getLastName())
            .isActive(Boolean.parseBoolean(TRAINER_SUMMARY_2.getIsActive()))
            .date(LocalDate.of(2024, 1, 2))
            .duration(60)
            .actionType(ActionType.DELETE)
            .build();

    public static final TrainingInfoDto TRAINING_DTO_INVALID_ACTION_TYPE = TrainingInfoDto.builder()
            .username(TRAINER_SUMMARY_1.getUsername())
            .firstName(TRAINER_SUMMARY_1.getFirstName())
            .lastName(TRAINER_SUMMARY_1.getLastName())
            .isActive(Boolean.parseBoolean(TRAINER_SUMMARY_1.getIsActive()))
            .date(LocalDate.of(2024, 1, 2))
            .duration(60)
            .build();
}
