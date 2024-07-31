package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.BaseTest;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;

public class TrainingTypeRepositoryTest extends BaseTest {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void getExisted() {
        TrainingType retrievedTrainingType = trainingTypeRepository.getExisted(TRAINING_TYPE_1_ID);
        TRAINING_TYPE_MATCHER.assertMatch(retrievedTrainingType, TRAINING_TYPE_1);
    }

    @Test
    void getExistedNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainingTypeRepository.getExisted(TRAINING_TYPE_1_ID + 10));
    }
}
