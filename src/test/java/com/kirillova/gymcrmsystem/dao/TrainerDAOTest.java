package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1_ID;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1_ID;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.kirillova.gymcrmsystem.TrainerTestData.getNewTrainer;
import static com.kirillova.gymcrmsystem.TrainerTestData.getUpdatedTrainer;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;

class TrainerDAOTest extends AbstractDAOTest {

    @Autowired
    private TrainerDAO trainerDAO;

    @Test
    void save() {
        Trainer savedTrainer = trainerDAO.save(getNewTrainer());
        int trainerId = savedTrainer.getId();
        Trainer newTrainer = getNewTrainer();
        newTrainer.setId(trainerId);

        TRAINER_MATCHER.assertMatch(savedTrainer, newTrainer);
        TRAINER_MATCHER.assertMatch(trainerDAO.get(trainerId), newTrainer);
    }

    @Test
    void update() {
        trainerDAO.update(getUpdatedTrainer());
        TRAINER_MATCHER.assertMatch(trainerDAO.get(TRAINER_1_ID), getUpdatedTrainer());
    }

    @Test
    void get() {
        Trainer retrievedTrainer = trainerDAO.get(TRAINER_1_ID);
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
    }

    @Test
    void getByUserId() {
        Trainer retrievedTrainer = trainerDAO.getByUserId(USER_5.getId());
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
    }

    @Test
    void getFreeTrainersForUsername() {
        List<Trainer> actual = trainerDAO.getFreeTrainersForUsername("Angelina.Jolie");
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_3);
        TRAINER_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getTrainersForTrainee() {
        List<Trainer> expected = trainerDAO.getTrainersForTrainee(TRAINEE_1_ID);
        List<Trainer> actual = Arrays.asList(TRAINER_2, TRAINER_4);
        TRAINER_MATCHER.assertMatch(actual, expected);
    }
}