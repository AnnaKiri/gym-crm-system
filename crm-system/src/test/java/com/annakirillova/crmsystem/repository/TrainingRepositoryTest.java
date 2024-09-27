package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.BaseTest;
import com.annakirillova.crmsystem.error.NotFoundException;
import com.annakirillova.crmsystem.models.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTraineeId;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTrainerId;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTypeId;

public class TrainingRepositoryTest extends BaseTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    void getFullTrainingById() {
        Training retrievedTraining = trainingRepository.getFullTrainingById(TRAINING_1_ID).orElse(null);
        TRAINING_MATCHER.assertMatch(retrievedTraining, TRAINING_1);
        checkTrainingTraineeId(TRAINING_1, retrievedTraining);
        checkTrainingTrainerId(TRAINING_1, retrievedTraining);
        checkTrainingTypeId(TRAINING_1, retrievedTraining);
    }

    @Test
    void getTrainingIfExistsWithWrongId() {
        Training retrievedTraining = trainingRepository.getTrainingIfExists(TRAINING_1_ID);
        TRAINING_MATCHER.assertMatch(retrievedTraining, TRAINING_1);
    }

    @Test
    void getTrainingIfExistsNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainingRepository.getTrainingIfExists(TRAINING_1_ID + 10));
    }
}
