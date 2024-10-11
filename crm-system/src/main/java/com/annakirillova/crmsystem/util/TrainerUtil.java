package com.annakirillova.crmsystem.util;

import com.annakirillova.crmsystem.dto.TraineeDto;
import com.annakirillova.crmsystem.dto.TrainerDto;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.User;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrainerUtil {

    public static TrainerDto createDtoWithTraineeToList(Trainer updatedTrainer, List<Trainee> traineeList) {
        User receivedUser = updatedTrainer.getUser();

        List<TraineeDto> traineeDtoList = TraineeUtil.getTraineeDtoList(traineeList);

        return TrainerDto.builder()
                .id(updatedTrainer.getId())
                .username(receivedUser.getUsername())
                .firstName(receivedUser.getFirstName())
                .lastName(receivedUser.getLastName())
                .specialization(updatedTrainer.getSpecialization())
                .specializationId(updatedTrainer.getSpecialization().getId())
                .isActive(receivedUser.isActive())
                .traineeList(traineeDtoList)
                .build();
    }

    public static List<TrainerDto> getTrainerDtoList(List<Trainer> trainerList) {
        List<TrainerDto> trainerDtoList = new ArrayList<>();

        for (Trainer trainer : trainerList) {
            User trainersUser = trainer.getUser();

            TrainerDto trainerDto = TrainerDto.builder()
                    .id(trainer.getId())
                    .username(trainersUser.getUsername())
                    .firstName(trainersUser.getFirstName())
                    .lastName(trainersUser.getLastName())
                    .specialization(trainer.getSpecialization())
                    .specializationId(trainer.getSpecialization().getId())
                    .isActive(trainersUser.isActive())
                    .build();

            trainerDtoList.add(trainerDto);
        }
        return trainerDtoList;
    }
}
