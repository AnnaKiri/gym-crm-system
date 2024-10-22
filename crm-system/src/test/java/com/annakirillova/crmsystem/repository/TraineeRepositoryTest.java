package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_2;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_3;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.annakirillova.crmsystem.TraineeTestData.checkTraineeUserId;
import static com.annakirillova.crmsystem.UserTestData.NOT_FOUND_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_6;

public class TraineeRepositoryTest extends BaseRepositoryTest {

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
    void getTraineeIfExists() {
        Trainee retrievedTrainee = traineeRepository.getTraineeIfExists(USER_1_USERNAME);
        TRAINEE_MATCHER.assertMatch(retrievedTrainee, TRAINEE_1);
    }

    @Test
    void getTraineeIfExistsWithWrongUsername() {
        Assertions.assertThrows(NotFoundException.class,
                () -> traineeRepository.getTraineeIfExists(NOT_FOUND_USERNAME));
    }
}
