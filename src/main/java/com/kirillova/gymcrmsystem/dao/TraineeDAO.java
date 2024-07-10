package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.service.TraineeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TraineeDAO {
    private static final Logger log = getLogger(TraineeService.class);

    private final Map<Long, Trainee> traineeStorage;
    private final AtomicLong index = new AtomicLong(0L);

    @Autowired
    public TraineeDAO(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public Trainee save(Trainee trainee) {
        long newId = index.incrementAndGet();
        trainee.setId(newId);
        traineeStorage.put(newId, trainee);
        log.debug("New trainee with id = " + newId + " saved");
        return trainee;
    }

    public void update(long traineeId, Trainee updatedTrainee) {
        traineeStorage.put(traineeId, updatedTrainee);
        log.debug("Trainee with id = " + traineeId + " updated");
    }

    public void delete(long traineeId) {
        traineeStorage.remove(traineeId);
        log.debug("Trainee with id = " + traineeId + " deleted");
    }

    public Trainee getTrainee(long traineeId) {
        log.debug("Get trainee with id = " + traineeId);
        return traineeStorage.get(traineeId);
    }
}

