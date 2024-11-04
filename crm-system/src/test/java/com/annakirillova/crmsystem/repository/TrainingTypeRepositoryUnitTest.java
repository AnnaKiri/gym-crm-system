package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_1_ID;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;

public class TrainingTypeRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void getTrainingTypeIfExistsWithWrongId() {
        TrainingType retrievedTrainingType = trainingTypeRepository.getTrainingTypeIfExists(TRAINING_TYPE_1_ID);
        TRAINING_TYPE_MATCHER.assertMatch(retrievedTrainingType, TRAINING_TYPE_1);
    }

    @Test
    void getTrainingTypeIfExistsNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainingTypeRepository.getTrainingTypeIfExists(TRAINING_TYPE_1_ID + 10));
    }
}
