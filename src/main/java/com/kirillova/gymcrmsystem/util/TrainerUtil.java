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

        TrainerTo filledTrainerTo = TrainerTo.builder().
                username(receivedUser.getUsername()).
                firstName(receivedUser.getFirstName()).
                lastName(receivedUser.getLastName()).
                specialization(updatedTrainer.getSpecialization()).
                isActive(receivedUser.isActive()).
                traineeList(traineeToList).
                build();

        return filledTrainerTo;
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
