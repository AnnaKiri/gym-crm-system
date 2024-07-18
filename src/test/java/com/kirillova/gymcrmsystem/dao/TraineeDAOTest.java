package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.kirillova.gymcrmsystem.TraineeTestData.getNewTrainee;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTrainee;

class TraineeDAOTest extends AbstractDAOTest {

    @Autowired
    private TraineeDAO traineeDAO;

    @Test
    void save() {
        Trainee savedTrainee = traineeDAO.save(getNewTrainee());
        int traineeId = savedTrainee.getId();
        Trainee newTrainee = getNewTrainee();
        newTrainee.setId(traineeId);

        TRAINEE_MATCHER.assertMatch(savedTrainee, newTrainee);
        TRAINEE_MATCHER.assertMatch(traineeDAO.get(traineeId), newTrainee);
    }

    @Test
    void update() {
        traineeDAO.update(getUpdatedTrainee());
        TRAINEE_MATCHER.assertMatch(traineeDAO.get(1), getUpdatedTrainee());
    }

//    @Test
//    void delete() {
//        traineeDAO.delete(traineeId);
//        assertFalse(traineeStorage.containsKey(traineeId));
//    }
//
//    @Test
//    void getTrainee() {
//        Trainee retrievedTrainee = traineeDAO.getTrainee(traineeId);
//        assertEquals(savedTrainee, retrievedTrainee);
//    }
}