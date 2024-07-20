package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_4;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_2;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_6;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTraineeId;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTrainerId;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTypeId;
import static com.kirillova.gymcrmsystem.TrainingTestData.getNewTraining;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;

class TrainingDAOTest extends AbstractDAOTest {

    @Autowired
    private TrainingDAO trainingDAO;

    @Test
    void save() {
        Training savedTraining = trainingDAO.save(getNewTraining());
        int trainingId = savedTraining.getId();
        Training newTraining = getNewTraining();
        newTraining.setId(trainingId);

        TRAINING_MATCHER.assertMatch(savedTraining, newTraining);
        checkTrainingTraineeId(newTraining, savedTraining);
        checkTrainingTrainerId(newTraining, savedTraining);
        checkTrainingTypeId(newTraining, savedTraining);

        Training training = trainingDAO.get(trainingId);

        TRAINING_MATCHER.assertMatch(training, newTraining);
        checkTrainingTraineeId(newTraining, training);
        checkTrainingTrainerId(newTraining, training);
        checkTrainingTypeId(newTraining, training);
    }

    @Test
    void get() {
        Training retrievedTraining = trainingDAO.get(TRAINING_1_ID);

        TRAINING_MATCHER.assertMatch(retrievedTraining, TRAINING_1);
        checkTrainingTraineeId(TRAINING_1, retrievedTraining);
        checkTrainingTrainerId(TRAINING_1, retrievedTraining);
        checkTrainingTypeId(TRAINING_1, retrievedTraining);
    }

    @Test
    void getTraineeTrainings() {
        List<Training> expected = trainingDAO.getTraineeTrainings(
                TRAINEE_1.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINING_TYPE_2.getName(),
                TRAINER_2.getUser().getFirstName(),
                TRAINER_2.getUser().getLastName());
        List<Training> actual = List.of(TRAINING_2);

        TRAINING_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getTraineeTrainingsWithoutParams() {
        List<Training> expected = trainingDAO.getTraineeTrainings(
                TRAINEE_1.getUser().getUsername(),
                null,
                null,
                null,
                null,
                null);
        List<Training> actual = List.of(TRAINING_1, TRAINING_2);

        TRAINING_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getTrainerTrainings() {
        List<Training> expected = trainingDAO.getTrainerTrainings(
                TRAINER_4.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINEE_1.getUser().getFirstName(),
                TRAINEE_1.getUser().getLastName());
        List<Training> actual = List.of(TRAINING_1);

        TRAINING_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getTrainerTrainingsWithoutParams() {
        List<Training> expected = trainingDAO.getTrainerTrainings(
                TRAINER_4.getUser().getUsername(),
                null,
                null,
                null,
                null);
        List<Training> actual = List.of(TRAINING_1, TRAINING_6);

        TRAINING_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }
}