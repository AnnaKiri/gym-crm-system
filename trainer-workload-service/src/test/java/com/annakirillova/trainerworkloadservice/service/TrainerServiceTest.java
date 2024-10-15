package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_2_USERNAME;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.getNewTrainer;
import static com.annakirillova.trainerworkloadservice.service.TrainerService.getExistingSummary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void create() {
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.<Trainer>getArgument(0));

        when(trainerRepository.findByUsername("Jim.Carrey")).thenReturn(Optional.empty());

        Trainer newTrainer = getNewTrainer();
        Trainer savedTrainer = trainerService.create("Jim", "Carrey", "Jim.Carrey", true);

        verify(trainerRepository, times(1)).save(any(Trainer.class));

        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(savedTrainer, newTrainer);
    }

    @Test
    void get() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_1);

        Trainer foundTrainer = trainerService.get(TRAINER_1_USERNAME);
        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(foundTrainer, TRAINER_1);
    }

    @Test
    void createExistedTrainer() {
        when(trainerRepository.findByUsername(TRAINER_1_USERNAME)).thenReturn(Optional.of(TRAINER_1));

        Trainer savedTrainer = trainerService.create(
                TRAINER_1.getFirstName(),
                TRAINER_1.getLastName(),
                TRAINER_1.getUsername(),
                true
        );

        verify(trainerRepository, times(0)).save(any(Trainer.class));

        TRAINER_MATCHER_WITH_SUMMARY_LIST.assertMatch(savedTrainer, TRAINER_1);
    }

    @Test
    void addTrainingDuration() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_1);
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.<Trainer>getArgument(0));

        LocalDate addedDate = LocalDate.of(2024, 3, 6);

        Trainer updatedTrainer = trainerService.addOrUpdateTrainingDuration(TRAINER_1_USERNAME, addedDate, 60);

        Optional<Trainer.Summary> existingSummary = getExistingSummary(updatedTrainer, addedDate);

        assertTrue(existingSummary.isPresent());
        assertEquals(60, existingSummary.get().getDuration());

        verify(trainerRepository, times(1)).save(TRAINER_1);
    }

    @Test
    void updateTrainingDuration() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_1);
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.<Trainer>getArgument(0));

        LocalDate updatedDate = LocalDate.of(2024, 2, 6);

        Trainer updatedTrainer = trainerService.addOrUpdateTrainingDuration(TRAINER_1_USERNAME, updatedDate, 60);

        Optional<Trainer.Summary> existingSummary = getExistingSummary(updatedTrainer, updatedDate);

        assertTrue(existingSummary.isPresent());
        assertEquals(120, existingSummary.get().getDuration());

        verify(trainerRepository, times(1)).save(TRAINER_1);
    }

    @Test
    void deleteTrainingDurationFromSummaryByDateAndUsername() {
        when(trainerRepository.getTrainerIfExists(TRAINER_2_USERNAME)).thenReturn(TRAINER_2);
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.<Trainer>getArgument(0));

        LocalDate deletedDate = LocalDate.of(2024, 1, 6);

        Trainer updatedTrainer = trainerService.deleteTrainingDurationFromSummaryByDateAndUsername(TRAINER_2_USERNAME, deletedDate, 60);

        Optional<Trainer.Summary> existingSummary = getExistingSummary(updatedTrainer, deletedDate);

        assertTrue(existingSummary.isPresent());
        assertEquals(60, existingSummary.get().getDuration());

        verify(trainerRepository, times(1)).save(TRAINER_2);
    }
}