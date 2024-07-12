package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
@RequiredArgsConstructor
public class TraineeDAO {
    private final Map<Long, Trainee> traineeStorage;
    private final AtomicLong index = new AtomicLong(0L);

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

