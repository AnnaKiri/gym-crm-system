package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.model.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_LIST_FOR_TRAINEE_2;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_MATCHER;

class TrainingRepositoryTest extends BaseTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    void getTrainingsByUsername() {
        List<Training> retrievedTrainings = trainingRepository.getTrainingsByUsername(TRAINER_2.getUsername());
        TRAINING_MATCHER.assertMatch(retrievedTrainings, TRAINING_LIST_FOR_TRAINEE_2);
    }

    @Test
    void deleteTrainingByDateAndDuration() {
        int changedLines = trainingRepository.deleteTrainingByDateAndDuration(TRAINER_1_USERNAME, LocalDate.of(2024, 1, 6), 60);
        Assertions.assertEquals(1, changedLines);
        Assertions.assertTrue(trainingRepository.getTrainingsByUsername(TRAINER_1_USERNAME).isEmpty());
    }
}