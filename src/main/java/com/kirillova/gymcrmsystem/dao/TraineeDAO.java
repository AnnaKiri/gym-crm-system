package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TraineeDAO {
    private final Map<Long, Trainee> traineeStorage;
    private final AtomicLong index = new AtomicLong(1L);

    @Autowired
    public TraineeDAO(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public Trainee save(Trainee trainee) {
        long newId = index.incrementAndGet();
        trainee.setId(newId);
        traineeStorage.put(newId, trainee);
        return trainee;
    }

    public void update(long traineeId, Trainee updatedTrainee) {
        traineeStorage.put(traineeId, updatedTrainee);
    }

    public void delete(long traineeId) {
        traineeStorage.remove(traineeId);
    }

    public Trainee getTrainee(long traineeId) {
        return traineeStorage.get(traineeId);
    }
}

