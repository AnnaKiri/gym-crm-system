package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrainingUtil {

    public static List<TrainingTo> getTrainingToList(List<Training> trainingList, String firstName, String lastName) {
        List<TrainingTo> trainingToList = new ArrayList<>();

        for (Training training : trainingList) {
            TrainingTo trainingTo = TrainingTo.builder().
                    name(training.getName()).
                    date(training.getDate()).
                    type(training.getType()).
                    duration(training.getDuration()).
                    traineeName(firstName + " " + lastName).
                    build();

            trainingToList.add(trainingTo);
        }
        return trainingToList;
    }

    public static TrainingTo createTo(Training training) {
        return TrainingTo.builder().
                name(training.getName()).
                date(training.getDate()).
                type(training.getType()).
                duration(training.getDuration()).
                traineeName(training.getTrainee().getUser().getUsername()).
                trainerName(training.getTrainer().getUser().getUsername()).
                build();

    }
}
