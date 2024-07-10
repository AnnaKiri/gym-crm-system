package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.service.TraineeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TrainingDAO {
    private static final Logger log = getLogger(TraineeService.class);

    private final Map<Long, Training> trainingStorage;
    private final AtomicLong index = new AtomicLong(0L);

    @Autowired
    public TrainingDAO(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training save(Training training) {
        long newId = index.incrementAndGet();
        training.setId(newId);
        trainingStorage.put(newId, training);
        log.debug("New training with id = " + newId + " saved");
        return training;
    }

    public Training getTraining(long trainingId) {
        log.debug("Get training with id = " + trainingId);
        return trainingStorage.get(trainingId);
    }
}
