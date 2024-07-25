package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.models.Trainee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_2;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.kirillova.gymcrmsystem.TraineeTestData.checkTraineeUserId;
import static com.kirillova.gymcrmsystem.TraineeTestData.getNewTrainee;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTrainee;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_6;
import static com.kirillova.gymcrmsystem.UserTestData.USER_9;

class TraineeDAOTest extends AbstractSpringTest {

    @Autowired
    private TraineeDAO traineeDAO;

    @Test
    void save() {
        Trainee savedTrainee = traineeDAO.save(getNewTrainee());
        int traineeId = savedTrainee.getId();
        Trainee newTrainee = getNewTrainee();
        newTrainee.setId(traineeId);

        TRAINEE_MATCHER.assertMatch(savedTrainee, newTrainee);
        TRAINEE_MATCHER.assertMatch(traineeDAO.get(USER_9.getUsername()), newTrainee);
        checkTraineeUserId(newTrainee, savedTrainee);
    }

    @Test
    void update() {
        Trainee updatedTraineeRef = getUpdatedTrainee();
        traineeDAO.update(updatedTraineeRef);

        Trainee updatedTrainee = traineeDAO.get(updatedTraineeRef.getUser().getUsername());

        TRAINEE_MATCHER.assertMatch(updatedTrainee, updatedTraineeRef);
        checkTraineeUserId(updatedTraineeRef, updatedTrainee);
    }

    @Test
    void get() {
        Trainee retrievedTrainee = traineeDAO.get(USER_1_USERNAME);

        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
        checkTraineeUserId(TRAINEE_1, retrievedTrainee);
    }

    @Test
    void getTraineesForTrainer() {
        List<Trainee> actual = traineeDAO.getTraineesForTrainer(USER_6.getUsername());
        List<Trainee> expected = List.of(TRAINEE_1, TRAINEE_2, TRAINEE_3);

        TRAINEE_MATCHER.assertMatch(actual, expected);
    }
}
