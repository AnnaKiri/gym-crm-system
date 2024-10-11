package com.annakirillova.crmsystem.util;

import com.annakirillova.crmsystem.dto.TrainingDto;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrainingUtil {

    public static List<TrainingDto> getTrainingDtoList(List<Training> trainingList) {
        List<TrainingDto> trainingDtoList = new ArrayList<>();

        for (Training training : trainingList) {
            Trainee trainee = training.getTrainee();
            Trainer trainer = training.getTrainer();
            TrainingDto trainingDto = TrainingDto.builder()
                    .id(training.getId())
                    .name(training.getName())
                    .date(training.getDate())
                    .type(training.getType())
                    .typeId(training.getType().getId())
                    .duration(training.getDuration())
                    .traineeUsername(trainee.getUser().getUsername())
                    .trainerUsername(trainer.getUser().getUsername())
                    .build();

            trainingDtoList.add(trainingDto);
        }
        return trainingDtoList;
    }

    public static TrainingDto createDto(Training training) {
        return TrainingDto.builder()
                .id(training.getId())
                .name(training.getName())
                .date(training.getDate())
                .type(training.getType())
                .typeId(training.getType().getId())
                .duration(training.getDuration())
                .traineeUsername(training.getTrainee().getUser().getUsername())
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .build();

    }
}
