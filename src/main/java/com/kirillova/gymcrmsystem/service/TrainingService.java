package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.TrainingTypeDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainingTypeDAO trainingTypeDAO;

    public Training get(int id) {
        log.debug("Get training with trainingId = {}", id);
        return checkNotFoundWithId(trainingDAO.get(id), id);
    }

    public Training getFull(int id) {
        log.debug("Get full training with trainingId = {}", id);
        return checkNotFoundWithId(trainingDAO.getFull(id), id);
    }

    public Training create(Trainee trainee, Trainer trainer, String name, Integer typeId, LocalDate date, int duration) {
        log.debug("Create new training");
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setName(name);
        training.setType(trainingTypeDAO.get(typeId));
        training.setDate(date);
        training.setDuration(duration);
        ValidationUtil.validate(training);
        return trainingDAO.save(training);
    }
}
