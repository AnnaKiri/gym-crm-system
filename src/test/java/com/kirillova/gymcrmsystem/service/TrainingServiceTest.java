package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.kirillova.gymcrmsystem.TestData.newTraining;
import static com.kirillova.gymcrmsystem.TestData.trainee3;
import static com.kirillova.gymcrmsystem.TestData.trainer3;
import static com.kirillova.gymcrmsystem.TestData.training1;
import static com.kirillova.gymcrmsystem.TestData.trainingType3;
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
        when(trainingDAO.getTraining(training1.getId())).thenReturn(training1);
        Assertions.assertEquals(training1, trainingService.get(training1.getId()));
    }

    @Test
    void create() {
        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(9L);
            return training;
        });
        Training result = trainingService.create(trainee3.getId(), trainer3.getId(), "Yoga", trainingType3.getId(), LocalDate.of(2024, 1, 5), 60);

        verify(trainingDAO, times(1)).save(any(Training.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(newTraining, result);
    }
}