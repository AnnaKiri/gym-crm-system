package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.config.ConfidentialProperties;
import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.config.SpringConfig;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class, ConfigurationProperties.class, ConfidentialProperties.class);

        TraineeService traineeService = context.getBean("TraineeService", TraineeService.class);
        TrainerService trainerService = context.getBean("TrainerService", TrainerService.class);
        TrainingService trainingService = context.getBean("TrainingService", TrainingService.class);

        System.out.println("First trainee: " + traineeService.get(1));
        System.out.println("First trainer: " + trainerService.get(1));
        System.out.println("First training: " + trainingService.get(1));

        context.close();
    }
}
