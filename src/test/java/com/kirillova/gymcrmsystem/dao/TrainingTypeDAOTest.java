package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.TrainingType;

import java.util.Map;

class TrainingTypeDAOTest {

    private Map<Long, TrainingType> trainingTypeStorage;
    private TrainingTypeDAO trainingTypeDAO;
    TrainingType testTrainingType;

//    @BeforeEach
//    public void setUp() {
//        trainingTypeStorage = new HashMap<>();
//        trainingTypeDAO = new TrainingTypeDAO(trainingTypeStorage, null);
//        testTrainingType = new TrainingType(trainingType1);
//    }
//
//    @Test
//    void save() {
//        TrainingType savedTrainingType = trainingTypeDAO.save(testTrainingType);
//        assertNotNull(savedTrainingType);
//        assertEquals(savedTrainingType, trainingType1);
//        assertTrue(trainingTypeStorage.containsKey(savedTrainingType.getId()));
//    }
}