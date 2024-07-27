package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("provideTraineesAndExpectedTrainers")
    void getFreeTrainersByUsername(Trainee trainee, List<Trainer> expected) {
        List<Trainer> actual = trainerDAO.getFreeTrainersForTrainee(trainee.getUser().getUsername());

        TRAINER_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }

    private static Stream<Arguments> provideTraineesAndExpectedTrainers() {
        return Stream.of(
                Arguments.of(TRAINEE_1, List.of(TRAINER_1, TRAINER_3)),
                Arguments.of(TRAINEE_2, List.of(TRAINER_1, TRAINER_4)),
                Arguments.of(TRAINEE_3, List.of(TRAINER_1, TRAINER_3, TRAINER_4)),
                Arguments.of(TRAINEE_4, List.of(TRAINER_2, TRAINER_3))
        );
    }

    @Test
    void getTrainersForTrainee() {
        List<Trainer> actual = trainerDAO.getTrainersForTrainee(USER_1.getUsername());
        List<Trainer> expected = List.of(TRAINER_2, TRAINER_4);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }
}
