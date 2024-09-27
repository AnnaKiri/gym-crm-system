package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.error.NotFoundException;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.NOT_FOUND_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_MATCHER;

class TrainerRepositoryTest extends BaseTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void findByUsername() {
        Trainer retrievedTrainer = trainerRepository.findByUsername(TRAINER_1_USERNAME).orElse(null);
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
    }

    @Test
    void getTrainerIfExists() {
        Trainer retrievedTrainer = trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME);
        TRAINER_MATCHER.assertMatch(retrievedTrainer, TRAINER_1);
    }

    @Test
    void getTraineeIfExistsWithWrongUsername() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainerRepository.getTrainerIfExists(NOT_FOUND_USERNAME));
    }

}