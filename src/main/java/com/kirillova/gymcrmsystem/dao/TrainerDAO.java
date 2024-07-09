package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerDAO {
    private final Map<Long, Trainer> trainerStorage;
    private Long index = 1L;

    @Autowired
    public TrainerDAO(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public Trainer save(Trainer trainer) {
        index++;
        trainer.setId(index);
        trainerStorage.put(index, trainer);
        return trainer;
    }

    public void update(long trainerId, Trainer updatedTrainer) {
        trainerStorage.put(trainerId, updatedTrainer);
    }

    public Trainer getTrainer(long trainerId) {
        return trainerStorage.get(trainerId);
    }
}
