package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_4;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;

class TrainingTypeDAOTest extends AbstractDAOTest {

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @Test
    void get() {
        List<TrainingType> actual = trainingTypeDAO.get();
        List<TrainingType> expected = Arrays.asList(TRAINING_TYPE_1, TRAINING_TYPE_2, TRAINING_TYPE_3, TRAINING_TYPE_4);

        TRAINING_TYPE_MATCHER.assertMatch(actual, expected);
    }
}