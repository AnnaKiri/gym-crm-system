package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.service.TrainerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TrainerDAO {
    private static final Logger log = getLogger(TrainerService.class);

    private final Map<Long, Trainer> trainerStorage;
    private final AtomicLong index = new AtomicLong(0L);

    @Autowired
    public TrainerDAO(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public Trainer save(Trainer trainer) {
        long newId = index.incrementAndGet();
        trainer.setId(newId);
        trainerStorage.put(newId, trainer);
        log.debug("New trainer with id = " + newId + " saved");
        return trainer;
    }

    public void update(long trainerId, Trainer updatedTrainer) {
        trainerStorage.put(trainerId, updatedTrainer);
        log.debug("Trainer with id = " + trainerId + " updated");
    }

    public Trainer getTrainer(long trainerId) {
        log.debug("Get trainer with id = " + trainerId);
        return trainerStorage.get(trainerId);
    }
}
