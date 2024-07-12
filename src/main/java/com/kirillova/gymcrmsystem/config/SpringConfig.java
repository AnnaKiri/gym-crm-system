package com.kirillova.gymcrmsystem.config;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan("com.kirillova.gymcrmsystem")
public class SpringConfig {
    @Bean
    public Map<Long, User> userStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Set<String> allUsernames() {
        return Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, TrainingType> trainingTypeStorage() {
        return new ConcurrentHashMap<>();
    }
}