package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.BaseTest;
import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

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

class TrainingDAOTest extends BaseTest {

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

    @ParameterizedTest
    @MethodSource("provideTraineeTrainingsParams")
    void getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainingType,
                             String trainerFirstName, String trainerLastName, List<Training> expected) {
        List<Training> actual = trainingDAO.getTraineeTrainings(
                traineeUsername, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);

        TRAINING_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    private static Stream<Arguments> provideTraineeTrainingsParams() {
        return Stream.of(
                Arguments.of(TRAINEE_1.getUser().getUsername(), LocalDate.of(2023, 1, 1),
                        LocalDate.of(2024, 1, 15), TRAINING_TYPE_2.getName(),
                        TRAINER_2.getUser().getFirstName(), TRAINER_2.getUser().getLastName(), List.of(TRAINING_2)),
                Arguments.of(TRAINEE_1.getUser().getUsername(), null, null, null, null, null,
                        List.of(TRAINING_1, TRAINING_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainerTrainingsParams")
    void getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                             String traineeFirstName, String traineeLastName, List<Training> expected) {
        List<Training> actual = trainingDAO.getTrainerTrainings(
                trainerUsername, fromDate, toDate, traineeFirstName, traineeLastName);

        TRAINING_MATCHER.assertMatch(actual, expected);

        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    private static Stream<Arguments> provideTrainerTrainingsParams() {
        return Stream.of(
                Arguments.of(TRAINER_4.getUser().getUsername(), LocalDate.of(2023, 1, 1),
                        LocalDate.of(2024, 1, 15), TRAINEE_1.getUser().getFirstName(),
                        TRAINEE_1.getUser().getLastName(), List.of(TRAINING_1)),
                Arguments.of(TRAINER_4.getUser().getUsername(), null, null, null, null,
                        List.of(TRAINING_1, TRAINING_6))
        );
    }
}
