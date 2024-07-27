package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
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

class TrainingTypeDAOTest extends AbstractSpringTest {

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @Test
    void getAll() {
        List<TrainingType> actual = trainingTypeDAO.getAll();
        List<TrainingType> expected = Arrays.asList(TRAINING_TYPE_1, TRAINING_TYPE_2, TRAINING_TYPE_3, TRAINING_TYPE_4);

        TRAINING_TYPE_MATCHER.assertMatch(actual, expected);
    }
}