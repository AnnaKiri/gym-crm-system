package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingDAO {

    private final Map<Long, Training> trainingStorage;
    private final AtomicLong index = new AtomicLong(0L);

    public Training save(Training training) {
        long newId = index.incrementAndGet();
        training.setId(newId);
        trainingStorage.put(newId, training);
        log.debug("New training with id = " + newId + " saved");
        return training;
    }

    public Training getTraining(long trainingId) {
        log.debug("Get training with id = " + trainingId);
        return trainingStorage.get(trainingId);
    }
}
