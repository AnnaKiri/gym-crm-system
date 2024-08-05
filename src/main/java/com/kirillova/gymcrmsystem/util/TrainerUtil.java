package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.dto.TraineeDto;
import com.kirillova.gymcrmsystem.dto.TrainerDto;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.User;
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
