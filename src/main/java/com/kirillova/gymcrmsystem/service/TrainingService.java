package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.AppConfig;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.models.Training;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TrainingService implements InitializingBean {
    private static final Logger log = getLogger(TrainingService.class);

    private final AppConfig appConfig;

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingService(AppConfig appConfig, TrainingDAO trainingDAO) {
        this.appConfig = appConfig;
        this.trainingDAO = trainingDAO;
    }

    public Training get(int trainingId) {
        log.debug("Get training with id = " + trainingId);
        return trainingDAO.getTraining(trainingId);
    }

    public Training create(long traineeId, long trainerId, String name, long typeId, LocalDate date, int duration) {
        log.debug("Create new training");
        Training training = new Training();
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setName(name);
        training.setTypeId(typeId);
        training.setLocalDate(date);
        training.setDuration(duration);
        return trainingDAO.save(training);
    }

    @Override
    public void afterPropertiesSet() {
        try (BufferedReader reader = new BufferedReader(new FileReader(appConfig.getTrainingDataPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); //traineeId, trainerId, name, typeId, date, duration
                create(Long.parseLong(parts[0]), Long.parseLong(parts[1]), parts[2], Long.parseLong(parts[3]), LocalDate.parse(parts[4]), Integer.parseInt(parts[5]));
            }
        } catch (IOException e) {
            log.debug("Error reading training init data file");
        }
    }
}
