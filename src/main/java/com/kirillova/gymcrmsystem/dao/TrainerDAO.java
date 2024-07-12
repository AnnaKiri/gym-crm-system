package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainerDAO {

    private final Map<Long, Trainer> trainerStorage;
    private final AtomicLong index = new AtomicLong(0L);


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
