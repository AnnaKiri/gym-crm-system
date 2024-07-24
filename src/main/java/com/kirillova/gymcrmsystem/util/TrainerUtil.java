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
public class TrainerUtil {

    public static TrainerTo createToWithTraineeToList(Trainer updatedTrainer) {
        User receivedUser = updatedTrainer.getUser();

        List<Trainee> traineeList = updatedTrainer.getTraineeList();

        List<TraineeTo> traineeToList = TraineeUtil.getTraineeToList(traineeList);

        return TrainerTo.builder().
                username(receivedUser.getUsername()).
                firstName(receivedUser.getFirstName()).
                lastName(receivedUser.getLastName()).
                specialization(updatedTrainer.getSpecialization()).
                isActive(receivedUser.isActive()).
                traineeList(traineeToList).
                build();
    }

    public static List<TrainerTo> getTrainerToList(List<Trainer> trainerList) {
        List<TrainerTo> trainerToList = new ArrayList<>();

        for (Trainer trainer : trainerList) {
            User trainersUser = trainer.getUser();

            TrainerTo trainerTo = TrainerTo.builder().
                    username(trainersUser.getUsername()).
                    firstName(trainersUser.getFirstName()).
                    lastName(trainersUser.getLastName()).
                    specialization(trainer.getSpecialization()).
                    build();

            trainerToList.add(trainerTo);
        }
        return trainerToList;
    }
}
