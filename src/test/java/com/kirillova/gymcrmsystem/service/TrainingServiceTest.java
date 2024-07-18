package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

//    @Test
//    void get() {
//        when(trainingDAO.getTraining(training1.getId())).thenReturn(training1);
//        Assertions.assertEquals(training1, trainingService.get(training1.getId()));
//    }
//
//    @Test
//    void create() {
//        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> {
//            Training training = invocation.getArgument(0);
//            training.setId(9L);
//            return training;
//        });
//        Training result = trainingService.create(trainee3.getId(), trainer3.getId(), "Yoga", trainingType3.getId(), LocalDate.of(2024, 1, 5), 60);
//
//        verify(trainingDAO, times(1)).save(any(Training.class));
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(newTraining, result);
//    }
}