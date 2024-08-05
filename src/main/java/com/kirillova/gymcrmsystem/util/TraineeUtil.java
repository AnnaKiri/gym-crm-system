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

    public static Trainee createNewFromDto(TraineeDto traineeDto) {
        User newUser = new User();
        newUser.setFirstName(traineeDto.getFirstName());
        newUser.setLastName(traineeDto.getLastName());
        newUser.setUsername(traineeDto.getFirstName() + "." + traineeDto.getLastName());
        newUser.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getBirthday());
        trainee.setUser(newUser);
        return trainee;
    }
}
