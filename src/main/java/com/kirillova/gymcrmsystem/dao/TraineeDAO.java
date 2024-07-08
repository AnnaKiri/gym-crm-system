package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TraineeDAO {
    private final Map<Long, Trainee> traineeStorage;

    @Autowired
    public TraineeDAO(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public Trainee save(Trainee trainee) {
        return traineeStorage.put(trainee.getId(), trainee);
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

