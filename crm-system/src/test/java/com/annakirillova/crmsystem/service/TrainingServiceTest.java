package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.repository.TrainingRepository;
import com.annakirillova.crmsystem.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_3;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_3;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTraineeId;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTrainerId;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTypeId;
import static com.annakirillova.crmsystem.TrainingTestData.getNewTraining;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private MessageSenderService messageSenderService;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void get() {
        when(trainingRepository.getTrainingIfExists(TRAINING_1_ID)).thenReturn(TRAINING_1);

        Training training = trainingService.get(TRAINING_1_ID);

        TRAINING_MATCHER.assertMatch(training, TRAINING_1);
        checkTrainingTraineeId(TRAINING_1, training);
        checkTrainingTrainerId(TRAINING_1, training);
        checkTrainingTypeId(TRAINING_1, training);
    }

    @Test
    void create() {
        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(TRAINING_1_ID + 8);
            return training;
        });

        when(trainingTypeRepository.getTrainingTypeIfExists(TRAINING_TYPE_3.getId())).thenReturn(TRAINING_TYPE_3);
        doNothing().when(messageSenderService).sendMessage(anyString(), any(TrainingInfoDto.class));

        Training newTraining = getNewTraining();
        Training savedTraining = trainingService.create(TRAINEE_3, TRAINER_3, "Yoga", TRAINING_TYPE_3.getId(), LocalDate.of(2024, 1, 5), 60);

        verify(trainingRepository, times(1)).save(any(Training.class));
        verify(messageSenderService, times(1)).sendMessage(anyString(), any(TrainingInfoDto.class));

        int trainingId = savedTraining.getId();
        newTraining.setId(trainingId);

        TRAINING_MATCHER.assertMatch(savedTraining, newTraining);
        checkTrainingTraineeId(newTraining, savedTraining);
        checkTrainingTrainerId(newTraining, savedTraining);
        checkTrainingTypeId(newTraining, savedTraining);
    }
}