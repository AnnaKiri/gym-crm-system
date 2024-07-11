package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kirillova.gymcrmsystem.TestData.trainee1;
import static com.kirillova.gymcrmsystem.TestData.updatedTrainee;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeDAOTest {

    private Map<Long, Trainee> traineeStorage;
    private TraineeDAO traineeDAO;
    Trainee testTrainee;
    Trainee savedTrainee;
    long traineeId;

    @BeforeEach
    public void setUp() {
        traineeStorage = new HashMap<>();
        traineeDAO = new TraineeDAO(traineeStorage);
        testTrainee = new Trainee(trainee1);
        savedTrainee = traineeDAO.save(testTrainee);
        traineeId = savedTrainee.getId();
    }

    @Test
    void save() {
        assertNotNull(savedTrainee);
        assertEquals(savedTrainee, trainee1);
        assertTrue(traineeStorage.containsKey(savedTrainee.getId()));
    }

    @Test
    void update() {
        traineeDAO.update(traineeId, updatedTrainee);
        assertEquals(updatedTrainee, traineeStorage.get(traineeId));
    }

    @Test
    void delete() {
        traineeDAO.delete(traineeId);
        assertFalse(traineeStorage.containsKey(traineeId));
    }

    @Test
    void getTrainee() {
        Trainee retrievedTrainee = traineeDAO.getTrainee(traineeId);
        assertEquals(savedTrainee, retrievedTrainee);
    }
}