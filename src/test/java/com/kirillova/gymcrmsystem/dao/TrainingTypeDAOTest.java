package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kirillova.gymcrmsystem.TestData.trainingType1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingTypeDAOTest {

    private Map<Long, TrainingType> trainingTypeStorage;
    private TrainingTypeDAO trainingTypeDAO;
    TrainingType testTrainingType;

    @BeforeEach
    public void setUp() {
        trainingTypeStorage = new HashMap<>();
        trainingTypeDAO = new TrainingTypeDAO(trainingTypeStorage, null);
        testTrainingType = new TrainingType(trainingType1);
    }

    @Test
    void save() {
        TrainingType savedTrainingType = trainingTypeDAO.save(testTrainingType);
        assertNotNull(savedTrainingType);
        assertEquals(savedTrainingType, trainingType1);
        assertTrue(trainingTypeStorage.containsKey(savedTrainingType.getId()));
    }
}