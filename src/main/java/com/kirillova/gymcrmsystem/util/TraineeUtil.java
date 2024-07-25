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
                    username(traineesUser.getUsername()).
                    firstName(traineesUser.getFirstName()).
                    lastName(traineesUser.getLastName()).
                    build();

            traineeToList.add(traineeTo);
        }
        return traineeToList;
    }
}
