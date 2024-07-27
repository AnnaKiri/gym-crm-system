package com.kirillova.gymcrmsystem.facade;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.service.AuthenticationService;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.kirillova.gymcrmsystem.TraineeTestData.checkTraineeUserId;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.kirillova.gymcrmsystem.TrainerTestData.checkTrainerSpecializationId;
import static com.kirillova.gymcrmsystem.TrainerTestData.checkTrainerUserId;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTraineeId;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTrainerId;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTypeId;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_3;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private GymFacade gymFacade;

    @Test
    void createTrainee() {
        gymFacade.createTrainee("New", "Trainee", LocalDate.of(1976, 4, 10), "some address");

        verify(traineeService, times(1)).create("New", "Trainee", LocalDate.of(1976, 4, 10), "some address");
    }

    @Test
    void updateTrainee() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());

        gymFacade.updateTrainee(USER_1.getUsername(), USER_1.getPassword(), "updatedFirstName", "updatedLastName", LocalDate.of(1976, 4, 10), "updated address", false);

        verify(traineeService, times(1)).update(USER_1.getUsername(), "updatedFirstName", "updatedLastName", LocalDate.of(1976, 4, 10), "updated address", false);
    }

    @Test
    void getTrainee() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());
        when(traineeService.get(USER_1.getUsername())).thenReturn(TRAINEE_1);

        Trainee actual = gymFacade.getTrainee(USER_1.getUsername(), USER_1.getPassword());

        verify(traineeService, times(1)).get(USER_1.getUsername());
        TRAINEE_MATCHER.assertMatch(actual, TRAINEE_1);
        checkTraineeUserId(TRAINEE_1, actual);
    }

    @Test
    void deleteTrainee() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());
        gymFacade.deleteTrainee(USER_1.getUsername(), USER_1.getPassword());

        verify(traineeService, times(1)).delete(USER_1.getUsername());
    }

    @Test
    void createTrainer() {
        gymFacade.createTrainer("New", "Trainer", TRAINING_TYPE_1.getId());

        verify(trainerService, times(1)).create("New", "Trainer", TRAINING_TYPE_1.getId());
    }

    @Test
    void updateTrainer() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());
        gymFacade.updateTrainer(USER_5.getUsername(), USER_5.getPassword(), "updatedFirstName", "updatedLastName", TRAINING_TYPE_2.getId(), false);

        verify(trainerService, times(1)).update(USER_5.getUsername(), "updatedFirstName", "updatedLastName", TRAINING_TYPE_2.getId(), false);
    }

    @Test
    void getTrainer() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());
        when(trainerService.get(USER_5.getUsername())).thenReturn(TRAINER_1);

        Trainer actual = gymFacade.getTrainer(USER_5.getUsername(), USER_5.getPassword());

        verify(trainerService, times(1)).get(USER_5.getUsername());
        TRAINER_MATCHER.assertMatch(actual, TRAINER_1);
        checkTrainerUserId(TRAINER_1, actual);
        checkTrainerSpecializationId(TRAINER_1, actual);
    }

    @Test
    void createTraining() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());
        gymFacade.createTraining(USER_3.getUsername(), USER_3.getPassword(), TRAINEE_3, TRAINER_3, "New Training", TRAINING_TYPE_3.getId(), LocalDate.of(2024, 1, 5), 60);

        verify(trainingService, times(1)).create(TRAINEE_3, TRAINER_3, "New Training", TRAINING_TYPE_3.getId(), LocalDate.of(2024, 1, 5), 60);
    }

    @Test
    void getTraining() {
        doNothing().when(authService).checkAuthenticatedUser(anyString(), anyString());

        when(trainingService.get(TRAINING_1_ID)).thenReturn(TRAINING_1);

        Training actual = gymFacade.getTraining(USER_1.getUsername(), USER_1.getPassword(), TRAINING_1_ID);

        verify(trainingService, times(1)).get(TRAINING_1_ID);
        TRAINING_MATCHER.assertMatch(actual, TRAINING_1);
        checkTrainingTraineeId(TRAINING_1, actual);
        checkTrainingTrainerId(TRAINING_1, actual);
        checkTrainingTypeId(TRAINING_1, actual);
    }
}