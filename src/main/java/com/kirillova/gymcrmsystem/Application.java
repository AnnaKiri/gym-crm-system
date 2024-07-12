package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.config.SpringConfig;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.models.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.Set;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class, ConfigurationProperties.class);

        Map<Long, User> userStorage = context.getBean("userStorage", Map.class);
        Set<String> allUsernames = context.getBean("allUsernames", Set.class);
        Map<Long, Trainee> traineeStorage = context.getBean("traineeStorage", Map.class);
        Map<Long, Trainer> trainerStorage = context.getBean("trainerStorage", Map.class);
        Map<Long, Training> trainingStorage = context.getBean("trainingStorage", Map.class);
        Map<Long, TrainingType> trainingTypeStorage = context.getBean("trainingTypeStorage", Map.class);

        System.out.println("User Storage: " + userStorage);
        System.out.println("All Usernames: " + allUsernames);
        System.out.println("Trainee Storage: " + traineeStorage);
        System.out.println("Trainer Storage: " + trainerStorage);
        System.out.println("Training Storage: " + trainingStorage);
        System.out.println("Training Type Storage: " + trainingTypeStorage);

        context.close();
    }
}
