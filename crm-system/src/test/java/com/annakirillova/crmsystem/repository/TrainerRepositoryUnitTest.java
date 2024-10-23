package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_3;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_4;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.annakirillova.crmsystem.TrainerTestData.checkTrainerSpecializationId;
import static com.annakirillova.crmsystem.TrainerTestData.checkTrainerUserId;
import static com.annakirillova.crmsystem.UserTestData.NOT_FOUND_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_5_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void findTraineesByTrainerUsername() {
        List<Trainer> actual = trainerRepository.findTrainersByTraineeUsername(USER_1_USERNAME);
        List<Trainer> expected = List.of(TRAINER_2, TRAINER_4);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainerUserId(expected.get(i), actual.get(i));
            checkTrainerSpecializationId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getWithUserAndSpecialization() {
        Trainer retrievedTrainer = trainerRepository.getWithUserAndSpecialization(USER_5_USERNAME).orElse(null);
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
        checkTrainerUserId(TRAINER_1, retrievedTrainer);
        checkTrainerSpecializationId(TRAINER_1, retrievedTrainer);
    }

    @Test
    void findByUsername() {
        Trainer retrievedTrainer = trainerRepository.findByUsername(USER_5_USERNAME).orElse(null);
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
    }

    @Test
    void getTrainerIfExists() {
        Trainer retrievedTrainer = trainerRepository.getTrainerIfExists(USER_5_USERNAME);
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
    }

    @Test
    void getTraineeIfExistsWithWrongUsername() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainerRepository.getTrainerIfExists(NOT_FOUND_USERNAME));
    }

    @Test
    void findTrainersNotAssignedToTrainee() {
        Specification<Trainer> spec = TrainerSpecifications.notAssignedToTrainee(USER_1_USERNAME);
        List<Trainer> actual = trainerRepository.findAll(spec);

        List<Trainer> expected = List.of(TRAINER_1, TRAINER_3);

        TRAINER_MATCHER.assertMatch(actual, expected);
        assertEquals(2, expected.size());
    }
}
