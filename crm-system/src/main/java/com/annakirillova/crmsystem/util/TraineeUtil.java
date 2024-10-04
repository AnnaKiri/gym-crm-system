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
public class TraineeUtil {

    public static TraineeDto createDtoWithTrainerDtoList(Trainee updatedTrainee, List<Trainer> trainerList) {
        User receivedUser = updatedTrainee.getUser();

        List<TrainerDto> trainerDtoList = TrainerUtil.getTrainerDtoList(trainerList);

        return TraineeDto.builder()
                .id(updatedTrainee.getId())
                .username(receivedUser.getUsername())
                .firstName(receivedUser.getFirstName())
                .lastName(receivedUser.getLastName())
                .birthday(updatedTrainee.getDateOfBirth())
                .address(updatedTrainee.getAddress())
                .isActive(receivedUser.isActive())
                .trainerList(trainerDtoList)
                .build();
    }

    public static List<TraineeDto> getTraineeDtoList(List<Trainee> traineeList) {
        List<TraineeDto> traineeDtoList = new ArrayList<>();

        for (Trainee trainee : traineeList) {
            User traineesUser = trainee.getUser();

            TraineeDto traineeDto = TraineeDto.builder()
                    .id(trainee.getId())
                    .username(traineesUser.getUsername())
                    .firstName(traineesUser.getFirstName())
                    .lastName(traineesUser.getLastName())
                    .birthday(trainee.getDateOfBirth())
                    .address(trainee.getAddress())
                    .isActive(traineesUser.isActive())
                    .build();

            traineeDtoList.add(traineeDto);
        }
        return traineeDtoList;
    }
}
