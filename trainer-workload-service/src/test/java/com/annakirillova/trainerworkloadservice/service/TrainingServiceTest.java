package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.error.NotFoundException;
import com.annakirillova.trainerworkloadservice.model.Training;
import com.annakirillova.trainerworkloadservice.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_3;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_MATCHER;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.checkTrainingTrainerId;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.getNewTraining;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void create() {
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(TRAINING_1_ID + 8);
            return training;
        });

        Training newTraining = getNewTraining();
        Training savedTraining = trainingService.create(TRAINER_3, LocalDate.of(2024, 3, 5), 60);

        verify(trainingRepository, times(1)).save(any(Training.class));

        int trainingId = savedTraining.getId();
        newTraining.setId(trainingId);

        TRAINING_MATCHER.assertMatch(savedTraining, newTraining);
        checkTrainingTrainerId(newTraining, savedTraining);
    }

    @Test
    void deleteByUsernameAndDateAndDuration() {
        when(trainingRepository.deleteTrainingByDateAndDuration(TRAINER_1_USERNAME, LocalDate.of(2024, 1, 6), 60)).thenReturn(1);

        trainingService.deleteByUsernameAndDateAndDuration(TRAINER_1_USERNAME, LocalDate.of(2024, 1, 6), 60);

        verify(trainingRepository, times(1)).deleteTrainingByDateAndDuration(TRAINER_1_USERNAME, LocalDate.of(2024, 1, 6), 60);

//        Надо?
        when(trainingRepository.getTrainingsByUsername(TRAINER_1_USERNAME)).thenThrow(new NotFoundException("Not found entity with " + TRAINER_1_USERNAME));
        assertThrows(NotFoundException.class, () -> trainingRepository.getTrainingsByUsername(TRAINER_1_USERNAME));
    }
}