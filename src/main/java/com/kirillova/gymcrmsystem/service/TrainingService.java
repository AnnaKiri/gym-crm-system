package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.util.DataLoaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService implements InitializingBean {

    private final ConfigurationProperties configurationProperties;
    private final TrainingDAO trainingDAO;

    public Training get(long trainingId) {
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
        training.setDate(date);
        training.setDuration(duration);
        return trainingDAO.save(training);
    }

    @Override
    public void afterPropertiesSet() {
        DataLoaderUtil.loadData(configurationProperties.getTrainingDataPath(), parts -> {
            // traineeId, trainerId, name, typeId, date, duration
            create(Long.parseLong(parts[0]), Long.parseLong(parts[1]), parts[2], Long.parseLong(parts[3]), LocalDate.parse(parts[4]), Integer.parseInt(parts[5]));
        });
    }
}
