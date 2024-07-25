package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.models.Trainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_2;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.kirillova.gymcrmsystem.TrainerTestData.checkTrainerSpecializationId;
import static com.kirillova.gymcrmsystem.TrainerTestData.checkTrainerUserId;
import static com.kirillova.gymcrmsystem.TrainerTestData.getNewTrainer;
import static com.kirillova.gymcrmsystem.TrainerTestData.getUpdatedTrainer;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static com.kirillova.gymcrmsystem.UserTestData.USER_9;

class TrainerDAOTest extends AbstractSpringTest {

    @Autowired
    private TrainerDAO trainerDAO;

    @Test
    void save() {
        Trainer savedTrainer = trainerDAO.save(getNewTrainer());
        int trainerId = savedTrainer.getId();
        Trainer newTrainer = getNewTrainer();
        newTrainer.setId(trainerId);
        Trainer trainer = trainerDAO.get(USER_9.getUsername());

        TRAINER_MATCHER.assertMatch(savedTrainer, newTrainer);
        TRAINER_MATCHER.assertMatch(trainer, newTrainer);
        checkTrainerUserId(newTrainer, trainer);
        checkTrainerSpecializationId(newTrainer, trainer);
    }

    @Test
    void update() {
        Trainer updatedTrainer = getUpdatedTrainer();
        trainerDAO.update(updatedTrainer);
        Trainer trainer = trainerDAO.get(USER_5.getUsername());

        TRAINER_MATCHER.assertMatch(trainer, updatedTrainer);
        checkTrainerUserId(updatedTrainer, trainer);
        checkTrainerSpecializationId(updatedTrainer, trainer);
    }

    @Test
    void get() {
        Trainer retrievedTrainer = trainerDAO.get(USER_5.getUsername());

        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
        checkTrainerUserId(TRAINER_1, retrievedTrainer);
        checkTrainerSpecializationId(TRAINER_1, retrievedTrainer);
    }

    @Test
    void getFreeTrainersByUsernameForTrainee1() {
        List<Trainer> actual = trainerDAO.getFreeTrainersForTrainee(TRAINEE_1.getUser().getUsername());
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_3);
        TRAINER_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getFreeTrainersByUsernameForTrainee2() {
        List<Trainer> actual = trainerDAO.getFreeTrainersForTrainee(TRAINEE_2.getUser().getUsername());
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_4);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getFreeTrainersByUsernameForTrainee3() {
        List<Trainer> actual = trainerDAO.getFreeTrainersForTrainee(TRAINEE_3.getUser().getUsername());
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_3, TRAINER_4);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getFreeTrainersByUsernameForTrainee4() {
        List<Trainer> actual = trainerDAO.getFreeTrainersForTrainee(TRAINEE_4.getUser().getUsername());
        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_3);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getTrainersForTrainee() {
        List<Trainer> actual = trainerDAO.getTrainersForTrainee(USER_1.getUsername());
        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_4);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }
}