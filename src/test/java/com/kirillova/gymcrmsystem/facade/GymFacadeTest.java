package com.kirillova.gymcrmsystem.facade;

import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

//    @Test
//    void createTrainee() {
//        gymFacade.createTrainee("New", "Trainee", LocalDate.of(1976, 4, 10), "some address");
//        verify(traineeService, times(1)).create("New", "Trainee", LocalDate.of(1976, 4, 10), "some address");
//    }
//
//    @Test
//    void updateTrainee() {
//        gymFacade.updateTrainee(1L, "updatedFirstName", "updatedLastName", LocalDate.of(1976, 4, 10), "updated address", false);
//        verify(traineeService, times(1)).update(1L, "updatedFirstName", "updatedLastName", LocalDate.of(1976, 4, 10), "updated address", false);
//    }
//
//    @Test
//    void getTrainee() {
//        gymFacade.getTrainee(1L);
//        verify(traineeService, times(1)).get(1L);
//    }
//
//    @Test
//    void deleteTrainee() {
//        gymFacade.deleteTrainee(1L);
//        verify(traineeService, times(1)).delete(1L);
//    }
//
//    @Test
//    void createTrainer() {
//        gymFacade.createTrainer("New", "Trainer", 1L);
//        verify(trainerService, times(1)).create("New", "Trainer", 1L);
//    }
//
//    @Test
//    void updateTrainer() {
//        gymFacade.updateTrainer(1L, "updatedFirstName", "updatedLastName", 2, false);
//        verify(trainerService, times(1)).update(1L, "updatedFirstName", "updatedLastName", 2, false);
//    }
//
//    @Test
//    void getTrainer() {
//        gymFacade.getTrainer(1L);
//        verify(trainerService, times(1)).get(1L);
//    }
//
//    @Test
//    void createTraining() {
//        gymFacade.createTraining(3L, 3L, "New Training", 3L, LocalDate.of(2024, 1, 5), 60);
//        verify(trainingService, times(1)).create(3L, 3L, "New Training", 3L, LocalDate.of(2024, 1, 5), 60);
//    }
//
//    @Test
//    void getTraining() {
//        gymFacade.getTraining(1L);
//        verify(trainingService, times(1)).get(1L);
//    }
}