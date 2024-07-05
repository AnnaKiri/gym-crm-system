package com.kirillova.gymcrmsystem.config;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan("com.kirillova.gymcrmsystem")
public class SpringConfig {

    User user1 = new User(1, "Angelina", "Jolie", "Angelina.Jolie", "password1", true);
    User user2 = new User(2, "Ryan", "Reynolds", "Ryan.Reynolds", "password2", true);
    User user3 = new User(3, "Tom", "Hardy", "Tom.Hardy", "password3", true);
    User user4 = new User(4, "Keanu", "Reeves", "Keanu.Reeves", "password4", true);
    User user5 = new User(5, "Tom", "Cruise", "Tom.Cruise", "password5", true);
    User user6 = new User(6, "Brad", "Pitt", "Brad.Pitt", "password6", true);
    User user7 = new User(7, "Jennifer", "Aniston", "Jennifer.Aniston", "password7", true);
    User user8 = new User(8, "Sandra", "Bullock", "Sandra.Bullock", "password7", true);

    TrainingType trainingType1 = new TrainingType(1, "Strength");
    TrainingType trainingType2 = new TrainingType(2, "Aerobic");
    TrainingType trainingType3 = new TrainingType(3, "Yoga");
    TrainingType trainingType4 = new TrainingType(4, "Stretching");

    Trainee trainee1 = new Trainee(1, LocalDate.of(1975, 6, 4), "some address", user1.getId());
    Trainee trainee2 = new Trainee(2, LocalDate.of(1976, 10, 23), "some address", user2.getId());
    Trainee trainee3 = new Trainee(3, LocalDate.of(1976, 9, 15), "some address", user3.getId());
    Trainee trainee4 = new Trainee(4, LocalDate.of(1964, 9, 2), "some address", user4.getId());

    Trainer trainer1 = new Trainer(3, trainingType1.getId(), user5.getId());
    Trainer trainer2 = new Trainer(1, trainingType2.getId(), user6.getId());
    Trainer trainer3 = new Trainer(2, trainingType3.getId(), user7.getId());
    Trainer trainer4 = new Trainer(3, trainingType4.getId(), user8.getId());

    Training training1 = new Training(1, trainee1.getId(), trainer4.getId(), "Stretching", trainingType4.getId(), LocalDate.of(2024, 1, 1), 60);
    Training training2 = new Training(2, trainee1.getId(), trainer2.getId(), "Aerobic", trainingType2.getId(), LocalDate.of(2024, 1, 2), 60);
    Training training3 = new Training(3, trainee2.getId(), trainer2.getId(), "Aerobic", trainingType2.getId(), LocalDate.of(2024, 1, 2), 60);
    Training training4 = new Training(4, trainee2.getId(), trainer3.getId(), "Yoga", trainingType3.getId(), LocalDate.of(2024, 1, 5), 60);
    Training training5 = new Training(5, trainee3.getId(), trainer1.getId(), "Strength", trainingType1.getId(), LocalDate.of(2024, 1, 5), 60);
    Training training6 = new Training(6, trainee4.getId(), trainer4.getId(), "Stretching", trainingType4.getId(), LocalDate.of(2024, 1, 1), 60);
    Training training7 = new Training(7, trainee4.getId(), trainer1.getId(), "Strength", trainingType1.getId(), LocalDate.of(2024, 1, 6), 60);
    Training training8 = new Training(8, trainee2.getId(), trainer3.getId(), "Yoga", trainingType3.getId(), LocalDate.of(2024, 1, 5), 60);


    @Bean
    public Map<Long, Trainee> traineeStorage() {
        Map<Long, Trainee> traineeStorage = new HashMap<>();
        traineeStorage.put(trainee1.getId(), trainee1);
        traineeStorage.put(trainee2.getId(), trainee2);
        traineeStorage.put(trainee3.getId(), trainee3);
        traineeStorage.put(trainee4.getId(), trainee4);
        return traineeStorage;
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        trainerStorage.put(trainer1.getId(), trainer1);
        trainerStorage.put(trainer2.getId(), trainer2);
        trainerStorage.put(trainer3.getId(), trainer3);
        return trainerStorage;
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        Map<Long, Training> trainingStorage = new HashMap<>();
        trainingStorage.put(training1.getId(), training1);
        trainingStorage.put(training2.getId(), training2);
        trainingStorage.put(training3.getId(), training3);
        trainingStorage.put(training4.getId(), training4);
        trainingStorage.put(training5.getId(), training5);
        trainingStorage.put(training6.getId(), training6);
        trainingStorage.put(training7.getId(), training7);
        trainingStorage.put(training8.getId(), training8);
        return trainingStorage;
    }
}