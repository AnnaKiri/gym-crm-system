package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerDAO {
    private final Map<Long, Trainer> trainerStorage;

    @Autowired
    public TrainerDAO(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public void save(Trainer trainer) {
        trainerStorage.put(trainer.getId(), trainer);
    }

    public void update(long trainerId, Trainer updatedTrainer) {
        trainerStorage.put(trainerId, updatedTrainer);
    }

    public void delete(long trainerId) {
        trainerStorage.remove(trainerId);
    }

    public Trainer getTrainer(long trainerId) {
        return trainerStorage.get(trainerId);
    }
}
