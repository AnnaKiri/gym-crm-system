package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_3;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.getNewTraining;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void get() {
        when(trainingDAO.get(TRAINING_1_ID)).thenReturn(TRAINING_1);
        TRAINING_MATCHER.assertMatch(trainingService.get(TRAINING_1_ID), TRAINING_1);
    }

    @Test
    void create() {
        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(TRAINING_1_ID + 8);
            return training;
        });

        Training newTraining = getNewTraining();
        Training savedTraining = trainingService.create(TRAINEE_3, TRAINER_3, "Yoga", TRAINING_TYPE_3, LocalDate.of(2024, 1, 5), 60);

        verify(trainingDAO, times(1)).save(any(Training.class));

        int trainingId = savedTraining.getId();
        newTraining.setId(trainingId);

        TRAINING_MATCHER.assertMatch(savedTraining, newTraining);
    }
}