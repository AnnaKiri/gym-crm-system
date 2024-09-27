package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.model.Summary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_6;
import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_LIST_FOR_TRAINER_2;
import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_MATCHER;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2_USERNAME;

class SummaryRepositoryTest extends BaseTest {

    @Autowired
    private SummaryRepository summaryRepository;

    @Test
    void findAllByTrainerUsername() {
        List<Summary> retrievedSummaries = summaryRepository.findAllByTrainerUsername(TRAINER_2_USERNAME);
        SUMMARY_MATCHER.assertMatch(retrievedSummaries, SUMMARY_LIST_FOR_TRAINER_2);
    }

    @Test
    void findByTrainerAndDate() {
        Optional<Summary> foundSummary = summaryRepository.findByTrainerAndDate(TRAINER_1_USERNAME, 2024, 1);
        Assertions.assertTrue(foundSummary.isPresent(), "Summary should be found");
        SUMMARY_MATCHER.assertMatch(foundSummary.get(), SUMMARY_6);
    }

    @Test
    void deleteTrainingDurationFromSummaryByDateAndUsername() {
        int changedLines = summaryRepository.deleteTrainingDurationFromSummaryByDateAndUsername(TRAINER_1_USERNAME, 2024, 1, 60);
        Assertions.assertEquals(1, changedLines);
        SUMMARY_MATCHER.assertMatch(summaryRepository.findAllByTrainerUsername(TRAINER_1_USERNAME), List.of(new Summary(6, TRAINER_1, 2024, 1, 0)));
    }
}