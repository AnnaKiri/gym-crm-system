package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import com.kirillova.gymcrmsystem.to.TrainerTo;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TraineeUtil {

    public static TraineeTo createToWithTrainerToList(Trainee updatedTrainee, List<Trainer> trainerList) {
        User receivedUser = updatedTrainee.getUser();

        List<TrainerTo> trainerToList = TrainerUtil.getTrainerToList(trainerList);

        return TraineeTo.builder().
                id(updatedTrainee.getId()).
                username(receivedUser.getUsername()).
                firstName(receivedUser.getFirstName()).
                lastName(receivedUser.getLastName()).
                birthday(updatedTrainee.getDateOfBirth()).
                address(updatedTrainee.getAddress()).
                isActive(receivedUser.isActive()).
                trainerList(trainerToList).
                build();
    }

    public static List<TraineeTo> getTraineeToList(List<Trainee> traineeList) {
        List<TraineeTo> traineeToList = new ArrayList<>();

        for (Trainee trainee : traineeList) {
            User traineesUser = trainee.getUser();

            TraineeTo traineeTo = TraineeTo.builder().
                    id(trainee.getId()).
                    username(traineesUser.getUsername()).
                    firstName(traineesUser.getFirstName()).
                    lastName(traineesUser.getLastName()).
                    birthday(trainee.getDateOfBirth()).
                    address(trainee.getAddress()).
                    isActive(traineesUser.isActive()).
                    build();

            traineeToList.add(traineeTo);
        }
        return traineeToList;
    }

    public static Trainee createNewFromTo(TraineeTo traineeTo) {
        User newUser = new User();
        newUser.setFirstName(traineeTo.getFirstName());
        newUser.setLastName(traineeTo.getLastName());
        newUser.setUsername(traineeTo.getFirstName() + "." + traineeTo.getLastName());
        newUser.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setAddress(traineeTo.getAddress());
        trainee.setDateOfBirth(traineeTo.getBirthday());
        trainee.setUser(newUser);
        return trainee;
    }
}
