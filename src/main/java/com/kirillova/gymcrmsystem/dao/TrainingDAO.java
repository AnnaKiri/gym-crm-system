package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TrainingDAO {
    private final Map<Long, Training> trainingStorage;
    private final AtomicLong index = new AtomicLong(1L);

    @Autowired
    public TrainingDAO(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training save(Training training) {
        long newId = index.incrementAndGet();
        training.setId(newId);
        trainingStorage.put(newId, training);
        return training;
    }

    public Training getTraining(long trainingId) {
        return trainingStorage.get(trainingId);
    }
}
