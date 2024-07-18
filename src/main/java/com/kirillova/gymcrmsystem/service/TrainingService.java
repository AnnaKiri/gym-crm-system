package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingDAO trainingDAO;

    public Training get(long trainingId) {
        log.debug("Get training with trainingId = " + trainingId);
        return trainingDAO.get(trainingId);
    }

    public Training create(Trainee trainee, Trainer trainer, String name, TrainingType type, Date date, int duration) {
        log.debug("Create new training");
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setName(name);
        training.setType(type);
        training.setDate(date);
        training.setDuration(duration);
        ValidationUtil.validate(training);
        return trainingDAO.save(training);
    }
}
