package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.models.Training;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TrainingService {
    private static final Logger log = getLogger(TrainingService.class);

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO) {
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
}
