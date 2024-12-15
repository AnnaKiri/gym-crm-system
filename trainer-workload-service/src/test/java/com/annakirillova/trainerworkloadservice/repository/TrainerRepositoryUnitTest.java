package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_1;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@TestPropertySource(properties = "de.flapdoodle.mongodb.embedded.version=4.4.10")
class TrainerRepositoryUnitTest {

    @Autowired
    private TrainerRepositoryMongoDb trainerRepositoryMongoDb;

    @BeforeEach
    void setUp() {
        trainerRepositoryMongoDb.save((TrainerSummaryDto) TRAINER_SUMMARY_1);
        trainerRepositoryMongoDb.save((TrainerSummaryDto) TRAINER_SUMMARY_2);
    }

    @Test
    void findByUsernameSuccess() {
        Optional<TrainerSummaryDto> foundTrainer = trainerRepositoryMongoDb.findByUsernameSpecial(TRAINER_SUMMARY_1.getUsername());

        assertTrue(foundTrainer.isPresent());
        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(foundTrainer.get(), TRAINER_SUMMARY_1);
    }

    @Test
    void findByUsernameNotFound() {
        Optional<TrainerSummaryDto> foundTrainer = trainerRepositoryMongoDb.findByUsernameSpecial("nonexistent");
        assertFalse(foundTrainer.isPresent());
    }

    @Test
    void getTrainerIfExistsSuccess() {
        TrainerSummaryDto foundTrainer = trainerRepositoryMongoDb.getTrainerIfExists(TRAINER_SUMMARY_1.getUsername());
        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(foundTrainer, TRAINER_SUMMARY_1);
    }

    @Test
    void getTrainerIfExistsNotFound() {
        assertThrows(NotFoundException.class, () -> trainerRepositoryMongoDb.getTrainerIfExists("nonexistent"));
    }
}
