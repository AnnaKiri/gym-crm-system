package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;

class TrainingTypeDAOTest extends AbstractDAOTest {

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @Test
    void get() {
        TrainingType retrievedTrainingType = trainingTypeDAO.get(TRAINING_TYPE_1_ID);
        TRAINING_TYPE_MATCHER.assertMatch(retrievedTrainingType, TRAINING_TYPE_1);
    }
}