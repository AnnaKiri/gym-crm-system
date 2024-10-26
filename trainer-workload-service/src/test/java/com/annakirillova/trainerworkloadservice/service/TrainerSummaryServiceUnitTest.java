package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummary;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_2_USERNAME;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_1;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;
import static com.annakirillova.trainerworkloadservice.TestData.getNewTrainer;
import static com.annakirillova.trainerworkloadservice.service.TrainerSummaryService.getExistingSummary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerSummaryServiceUnitTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerSummaryService trainerSummaryService;

    @Test
    void create() {
        when(trainerRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.<TrainerSummary>getArgument(0));

        when(trainerRepository.findByUsername("Jim.Carrey")).thenReturn(Optional.empty());

        TrainerSummary newTrainerSummary = getNewTrainer();
        TrainerSummary savedTrainerSummary = trainerSummaryService.create("Jim", "Carrey", "Jim.Carrey", true);

        verify(trainerRepository, times(1)).save(any(TrainerSummary.class));

        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(savedTrainerSummary, newTrainerSummary);
    }

    @Test
    void get() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_SUMMARY_1);

        TrainerSummary foundTrainerSummary = trainerSummaryService.get(TRAINER_1_USERNAME);
        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(foundTrainerSummary, TRAINER_SUMMARY_1);
    }

    @Test
    void createExistedTrainer() {
        when(trainerRepository.findByUsername(TRAINER_1_USERNAME)).thenReturn(Optional.of(TRAINER_SUMMARY_1));

        TrainerSummary savedTrainerSummary = trainerSummaryService.create(
                TRAINER_SUMMARY_1.getFirstName(),
                TRAINER_SUMMARY_1.getLastName(),
                TRAINER_SUMMARY_1.getUsername(),
                true
        );

        verify(trainerRepository, times(0)).save(any(TrainerSummary.class));

        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(savedTrainerSummary, TRAINER_SUMMARY_1);
    }

    @Test
    void addTrainingDuration() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_SUMMARY_1);
        when(trainerRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.<TrainerSummary>getArgument(0));

        LocalDate addedDate = LocalDate.of(2024, 3, 6);

        TrainerSummary updatedTrainerSummary = trainerSummaryService.addOrUpdateTrainingDuration(TRAINER_1_USERNAME, addedDate, 60);

        Optional<TrainerSummaryDto.Summary> existingSummary = getExistingSummary(updatedTrainerSummary, addedDate);

        assertTrue(existingSummary.isPresent());
        assertEquals(60, existingSummary.get().getDuration());

        verify(trainerRepository, times(1)).save(TRAINER_SUMMARY_1);
    }

    @Test
    void updateTrainingDuration() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_SUMMARY_1);
        when(trainerRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.<TrainerSummary>getArgument(0));

        LocalDate updatedDate = LocalDate.of(2024, 2, 6);

        TrainerSummary updatedTrainerSummary = trainerSummaryService.addOrUpdateTrainingDuration(TRAINER_1_USERNAME, updatedDate, 60);

        Optional<TrainerSummaryDto.Summary> existingSummary = getExistingSummary(updatedTrainerSummary, updatedDate);

        assertTrue(existingSummary.isPresent());
        assertEquals(120, existingSummary.get().getDuration());

        verify(trainerRepository, times(1)).save(TRAINER_SUMMARY_1);
    }

    @Test
    void deleteTrainingDurationFromSummaryByDateAndUsername() {
        when(trainerRepository.getTrainerIfExists(TRAINER_2_USERNAME)).thenReturn(TRAINER_SUMMARY_2);
        when(trainerRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.<TrainerSummary>getArgument(0));

        LocalDate deletedDate = LocalDate.of(2024, 1, 6);

        TrainerSummary updatedTrainerSummary = trainerSummaryService.deleteTrainingDurationFromSummaryByDateAndUsername(TRAINER_2_USERNAME, deletedDate, 60);

        Optional<TrainerSummaryDto.Summary> existingSummary = getExistingSummary(updatedTrainerSummary, deletedDate);

        assertTrue(existingSummary.isPresent());
        assertEquals(60, existingSummary.get().getDuration());

        verify(trainerRepository, times(1)).save(TRAINER_SUMMARY_2);
    }
}