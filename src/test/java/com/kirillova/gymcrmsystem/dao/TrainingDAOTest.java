package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kirillova.gymcrmsystem.TestData.training1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingDAOTest {

    private Map<Long, Training> trainingStorage;
    private TrainingDAO trainingDAO;
    Training testTraining;
    Training savedTraining;
    long trainingId;

    @BeforeEach
    public void setUp() {
        trainingStorage = new HashMap<>();
        trainingDAO = new TrainingDAO(trainingStorage);
        testTraining = new Training(training1);
        savedTraining = trainingDAO.save(testTraining);
        trainingId = savedTraining.getId();
    }

    @Test
    void save() {
        assertNotNull(savedTraining);
        assertEquals(savedTraining, training1);
        assertTrue(trainingStorage.containsKey(savedTraining.getId()));
    }

    @Test
    void getTraining() {
        Training retrievedTraining = trainingDAO.getTraining(trainingId);
        assertEquals(savedTraining, retrievedTraining);
    }
}