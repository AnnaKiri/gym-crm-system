package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertEquals(newTraining.getTrainee().getId(), savedTraining.getTrainee().getId());
        Assertions.assertEquals(newTraining.getTrainer().getId(), savedTraining.getTrainer().getId());
        Assertions.assertEquals(newTraining.getType().getId(), savedTraining.getType().getId());

        Training training = trainingDAO.get(trainingId);
        TRAINING_MATCHER.assertMatch(training, newTraining);

        Assertions.assertEquals(newTraining.getTrainee().getId(), training.getTrainee().getId());
        Assertions.assertEquals(newTraining.getTrainer().getId(), training.getTrainer().getId());
        Assertions.assertEquals(newTraining.getType().getId(), training.getType().getId());
    }

    @Test
    void get() {
        Training retrievedTraining = trainingDAO.get(TRAINING_1_ID);
        TRAINING_MATCHER.assertMatch(retrievedTraining, TRAINING_1);
        Assertions.assertEquals(TRAINING_1.getTrainee().getId(), retrievedTraining.getTrainee().getId());
        Assertions.assertEquals(TRAINING_1.getTrainer().getId(), retrievedTraining.getTrainer().getId());
        Assertions.assertEquals(TRAINING_1.getType().getId(), retrievedTraining.getType().getId());
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
            Assertions.assertEquals(expected.get(i).getTrainee().getId(), actual.get(i).getTrainee().getId());
            Assertions.assertEquals(expected.get(i).getTrainer().getId(), actual.get(i).getTrainer().getId());
            Assertions.assertEquals(expected.get(i).getType().getId(), actual.get(i).getType().getId());
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
            Assertions.assertEquals(expected.get(i).getTrainee().getId(), actual.get(i).getTrainee().getId());
            Assertions.assertEquals(expected.get(i).getTrainer().getId(), actual.get(i).getTrainer().getId());
            Assertions.assertEquals(expected.get(i).getType().getId(), actual.get(i).getType().getId());
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
            Assertions.assertEquals(expected.get(i).getTrainee().getId(), actual.get(i).getTrainee().getId());
            Assertions.assertEquals(expected.get(i).getTrainer().getId(), actual.get(i).getTrainer().getId());
            Assertions.assertEquals(expected.get(i).getType().getId(), actual.get(i).getType().getId());
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
            Assertions.assertEquals(expected.get(i).getTrainee().getId(), actual.get(i).getTrainee().getId());
            Assertions.assertEquals(expected.get(i).getTrainer().getId(), actual.get(i).getTrainer().getId());
            Assertions.assertEquals(expected.get(i).getType().getId(), actual.get(i).getType().getId());
        }
    }
}