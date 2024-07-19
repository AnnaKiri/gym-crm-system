package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1_ID;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.kirillova.gymcrmsystem.TraineeTestData.getNewTrainee;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTrainee;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_ID;

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
        TRAINEE_MATCHER.assertMatch(traineeDAO.get(TRAINEE_1_ID), getUpdatedTrainee());
    }

    @Test
    void delete() {
        traineeDAO.delete(TRAINEE_1_ID);
        Assertions.assertNull(traineeDAO.get(TRAINEE_1_ID));
    }

    @Test
    void get() {
        Trainee retrievedTrainee = traineeDAO.get(TRAINEE_1_ID);
        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
    }

    @Test
    void getByUserId() {
        Trainee retrievedTrainee = traineeDAO.getByUserId(USER_1_ID);
        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
    }
}