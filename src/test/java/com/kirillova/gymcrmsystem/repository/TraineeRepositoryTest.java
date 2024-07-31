package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.BaseTest;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.Trainee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_2;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.kirillova.gymcrmsystem.TraineeTestData.checkTraineeUserId;
import static com.kirillova.gymcrmsystem.UserTestData.NOT_FOUND_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_6;

public class TraineeRepositoryTest extends BaseTest {

    @Autowired
    private TraineeRepository traineeRepository;

    @Test
    void findTraineesByTrainerUsername() {
        List<Trainee> actual = traineeRepository.findTraineesByTrainerUsername(USER_6.getUsername());
        List<Trainee> expected = List.of(TRAINEE_1, TRAINEE_2, TRAINEE_3);

        TRAINEE_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getWithUser() {
        Trainee retrievedTrainee = traineeRepository.getWithUser(USER_1_USERNAME).orElse(null);

        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
        checkTraineeUserId(TRAINEE_1, retrievedTrainee);
    }

    @Test
    void findByUsername() {
        Trainee retrievedTrainee = traineeRepository.findByUsername(USER_1_USERNAME).orElse(null);
        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
    }

    @Test
    void getExisted() {
        Trainee retrievedTrainee = traineeRepository.getExisted(USER_1_USERNAME);
        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
    }

    @Test
    void getExistedNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> traineeRepository.getExisted(NOT_FOUND_USERNAME));
    }
}
