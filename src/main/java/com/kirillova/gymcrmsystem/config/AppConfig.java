package com.kirillova.gymcrmsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${storage.init.data.trainee}")
    private String traineeDataPath;

    @Value("${storage.init.data.trainer}")
    private String trainerDataPath;

    @Value("${storage.init.data.training}")
    private String trainingDataPath;

    @Value("${storage.init.data.trainingtype}")
    private String trainingTypeDataPath;

    public String getTraineeDataPath() {
        return traineeDataPath;
    }

    public String getTrainerDataPath() {
        return trainerDataPath;
    }

    public String getTrainingDataPath() {
        return trainingDataPath;
    }

    public String getTrainingTypeDataPath() {
        return trainingTypeDataPath;
    }
}
