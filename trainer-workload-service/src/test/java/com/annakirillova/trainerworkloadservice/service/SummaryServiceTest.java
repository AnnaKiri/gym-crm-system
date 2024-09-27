package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.model.Summary;
import com.annakirillova.trainerworkloadservice.repository.SummaryRepository;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_3;
import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_6;
import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_LIST_FOR_TRAINER_1;
import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_MATCHER;
import static com.annakirillova.trainerworkloadservice.SummaryTestData.getUpdatedSummary;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock
    private SummaryRepository summaryRepository;

    @Mock
    private TrainerRepository trainerRepository;


    @InjectMocks
    private SummaryService summaryService;

    @Test
    void getMonthlyTrainingSummary() {
        when(summaryRepository.findAllByTrainerUsername(TRAINER_1_USERNAME)).thenReturn(SUMMARY_LIST_FOR_TRAINER_1);

        List<Summary> summaryList = summaryService.getMonthlyTrainingSummary(TRAINER_1_USERNAME);
        SUMMARY_MATCHER.assertMatch(summaryList, SUMMARY_LIST_FOR_TRAINER_1);
    }

    @Test
    void deleteTrainingDurationFromSummaryByDateAndUsername() {
        when(summaryRepository.deleteTrainingDurationFromSummaryByDateAndUsername(TRAINER_2_USERNAME, 2024, 2, 60)).thenReturn(1);

        summaryService.deleteTrainingDurationFromSummaryByDateAndUsername(TRAINER_2_USERNAME, LocalDate.of(2024, 2, 6), 60);

        verify(summaryRepository, times(1)).deleteTrainingDurationFromSummaryByDateAndUsername(TRAINER_2_USERNAME, 2024, 2, 60);

        when(summaryRepository.findAllByTrainerUsername(TRAINER_2_USERNAME)).thenReturn(List.of(SUMMARY_3));
        SUMMARY_MATCHER.assertMatch(summaryRepository.findAllByTrainerUsername(TRAINER_2_USERNAME), List.of(SUMMARY_3));
    }

    @Test
    void addTrainingDuration() {
        when(summaryRepository.findByTrainerAndDate(TRAINER_1_USERNAME, 2024, 2)).thenReturn(Optional.empty());
        when(trainerRepository.findByUsername(TRAINER_1_USERNAME)).thenReturn(Optional.of(TRAINER_1));

        summaryService.addOrUpdateTrainingDuration(TRAINER_1_USERNAME, LocalDate.of(2024, 2, 6), 60);

        verify(summaryRepository, times(1)).save(any(Summary.class));

        when(summaryRepository.findAllByTrainerUsername(TRAINER_1_USERNAME)).thenReturn(SUMMARY_LIST_FOR_TRAINER_1);
        SUMMARY_MATCHER.assertMatch(summaryRepository.findAllByTrainerUsername(TRAINER_1_USERNAME), SUMMARY_LIST_FOR_TRAINER_1);
    }

    @Test
    void UpdateTrainingDuration() {
        when(summaryRepository.findByTrainerAndDate(TRAINER_1_USERNAME, 2024, 1)).thenReturn(Optional.of(SUMMARY_6));

        summaryService.addOrUpdateTrainingDuration(TRAINER_1_USERNAME, LocalDate.of(2024, 1, 6), 60);

        verify(summaryRepository, times(1)).save(any(Summary.class));

        when(summaryRepository.findAllByTrainerUsername(TRAINER_1_USERNAME)).thenReturn(getUpdatedSummary());
        SUMMARY_MATCHER.assertMatch(summaryRepository.findAllByTrainerUsername(TRAINER_1_USERNAME), getUpdatedSummary());
    }
}