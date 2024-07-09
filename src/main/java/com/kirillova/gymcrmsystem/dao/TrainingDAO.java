package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainingDAO {
    private final Map<Long, Training> trainingStorage;
    private Long index = 1L;

    @Autowired
    public TrainingDAO(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training save(Training training) {
        index++;
        training.setId(index);
        trainingStorage.put(index, training);
        return training;
    }

    public Training getTraining(long trainingId) {
        return trainingStorage.get(trainingId);
    }
}
