package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;

import java.util.Map;

class TrainingDAOTest {

    private Map<Long, Training> trainingStorage;
    private TrainingDAO trainingDAO;
    Training testTraining;
    Training savedTraining;
    long trainingId;

//    @BeforeEach
//    public void setUp() {
//        trainingStorage = new HashMap<>();
//        trainingDAO = new TrainingDAO(trainingStorage);
//        testTraining = new Training(training1);
//        savedTraining = trainingDAO.save(testTraining);
//        trainingId = savedTraining.getId();
//    }
//
//    @Test
//    void save() {
//        assertNotNull(savedTraining);
//        assertEquals(savedTraining, training1);
//        assertTrue(trainingStorage.containsKey(savedTraining.getId()));
//    }
//
//    @Test
//    void getTraining() {
//        Training retrievedTraining = trainingDAO.getTraining(trainingId);
//        assertEquals(savedTraining, retrievedTraining);
//    }
}