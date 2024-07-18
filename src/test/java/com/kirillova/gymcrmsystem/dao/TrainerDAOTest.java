package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;

import java.util.Map;

class TrainerDAOTest {

    private Map<Long, Trainer> trainerStorage;
    private TrainerDAO trainerDAO;
    Trainer testTrainer;
    Trainer savedTrainer;
    long trainerId;

//    @BeforeEach
//    public void setUp() {
//        trainerStorage = new HashMap<>();
//        trainerDAO = new TrainerDAO(trainerStorage);
//        testTrainer = new Trainer(trainer1);
//        savedTrainer = trainerDAO.save(testTrainer);
//        trainerId = savedTrainer.getId();
//    }
//
//    @Test
//    void save() {
//        assertNotNull(savedTrainer);
//        assertEquals(savedTrainer, trainer1);
//        assertTrue(trainerStorage.containsKey(savedTrainer.getId()));
//    }
//
//    @Test
//    void update() {
//        trainerDAO.update(trainerId, updatedTrainer);
//        assertEquals(updatedTrainer, trainerStorage.get(trainerId));
//    }
//
//    @Test
//    void getTrainee() {
//        Trainer retrievedTrainer = trainerDAO.getTrainer(trainerId);
//        assertEquals(savedTrainer, retrievedTrainer);
//    }
}