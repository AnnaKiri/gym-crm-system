package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrainingUtil {

    public static List<TrainingTo> getTrainingToList(List<Training> trainingList) {
        List<TrainingTo> trainingToList = new ArrayList<>();

        for (Training training : trainingList) {
            Trainee trainee = training.getTrainee();
            Trainer trainer = training.getTrainer();
            TrainingTo trainingTo = TrainingTo.builder().
                    id(training.getId()).
                    name(training.getName()).
                    date(training.getDate()).
                    type(training.getType()).
                    duration(training.getDuration()).
                    traineeName(trainee.getUser().getUsername()).
                    trainerName(trainer.getUser().getUsername()).
                    build();

            trainingToList.add(trainingTo);
        }
        return trainingToList;
    }

    public static TrainingTo createTo(Training training) {
        return TrainingTo.builder().
                id(training.getId()).
                name(training.getName()).
                date(training.getDate()).
                type(training.getType()).
                duration(training.getDuration()).
                traineeName(training.getTrainee().getUser().getUsername()).
                trainerName(training.getTrainer().getUser().getUsername()).
                build();

    }
}
