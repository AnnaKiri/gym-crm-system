package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.models.User;

import java.util.Date;

public class TestData {
    public static final User user1 = new User(1, "Angelina", "Jolie", "Angelina.Jolie", "password1", true);
    public static final User user2 = new User(2, "Ryan", "Reynolds", "Ryan.Reynolds", "password2", true);
    public static final User user3 = new User(3, "Tom", "Hardy", "Tom.Hardy", "password3", true);
    public static final User user4 = new User(4, "Keanu", "Reeves", "Keanu.Reeves", "password4", true);
    public static final User user5 = new User(5, "Tom", "Cruise", "Tom.Cruise", "password5", true);
    public static final User user6 = new User(6, "Brad", "Pitt", "Brad.Pitt", "password6", true);
    public static final User user7 = new User(7, "Jennifer", "Aniston", "Jennifer.Aniston", "password7", true);
    public static final User user8 = new User(8, "Sandra", "Bullock", "Sandra.Bullock", "password8", true);
    public static final User newUser = new User(9, "Matthew", "Mcconaughey", "Matthew.Mcconaughey", "password9", true);
    public static final User updatedUserForTrainee = new User(1, "updatedFirstName", "updatedLastName", "Angelina.Jolie", "password1", false);
    public static final User updatedUserForTrainer = new User(5, "updatedFirstName", "updatedLastName", "Tom.Cruise", "password5", false);

    public static final TrainingType trainingType1 = new TrainingType(1, "Strength");
    public static final TrainingType trainingType2 = new TrainingType(2, "Aerobic");
    public static final TrainingType trainingType3 = new TrainingType(3, "Yoga");
    public static final TrainingType trainingType4 = new TrainingType(4, "Stretching");
    public static final TrainingType newTrainingType = new TrainingType(5, "New Training Type");

    public static final Trainee trainee1 = new Trainee(1, new Date(1975, 6, 4), "some address", user1);
    public static final Trainee trainee2 = new Trainee(2, new Date(1976, 10, 23), "some address", user2);
    public static final Trainee trainee3 = new Trainee(3, new Date(1976, 9, 15), "some address", user3);
    public static final Trainee trainee4 = new Trainee(4, new Date(1964, 9, 2), "some address", user4);
    public static final Trainee newTrainee = new Trainee(5, new Date(1969, 11, 4), "some new address", newUser);
    public static final Trainee updatedTrainee = new Trainee(1, new Date(1976, 4, 10), "updated address", updatedUserForTrainee);

    public static final Trainer trainer1 = new Trainer(1, trainingType1, user5);
    public static final Trainer trainer2 = new Trainer(2, trainingType2, user6);
    public static final Trainer trainer3 = new Trainer(3, trainingType3, user7);
    public static final Trainer trainer4 = new Trainer(4, trainingType4, user8);
    public static final Trainer newTrainer = new Trainer(5, trainingType4, newUser);
    public static final Trainer updatedTrainer = new Trainer(1, trainingType2, updatedUserForTrainer);

    public static final Training training1 = new Training(1, trainee1, trainer4, "Stretching", trainingType4, new Date(2024, 1, 1), 60);
    public static final Training training2 = new Training(2, trainee1, trainer2, "Aerobic", trainingType2, new Date(2024, 1, 2), 60);
    public static final Training training3 = new Training(3, trainee2, trainer2, "Aerobic", trainingType2, new Date(2024, 1, 2), 60);
    public static final Training training4 = new Training(4, trainee2, trainer3, "Yoga", trainingType3, new Date(2024, 1, 5), 60);
    public static final Training training5 = new Training(5, trainee3, trainer1, "Strength", trainingType1, new Date(2024, 1, 5), 60);
    public static final Training training6 = new Training(6, trainee4, trainer4, "Stretching", trainingType4, new Date(2024, 1, 1), 60);
    public static final Training training7 = new Training(7, trainee4, trainer1, "Strength", trainingType1, new Date(2024, 1, 6), 60);
    public static final Training training8 = new Training(8, trainee2, trainer3, "Yoga", trainingType3, new Date(2024, 1, 5), 60);
    public static final Training newTraining = new Training(9, trainee3, trainer3, "Yoga", trainingType3, new Date(2024, 1, 5), 60);
}
