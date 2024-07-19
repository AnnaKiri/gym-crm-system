package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1_ID;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.kirillova.gymcrmsystem.TraineeTestData.getNewTrainee;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTrainee;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_2;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_ID;
import static com.kirillova.gymcrmsystem.UserTestData.USER_LIST;
import static com.kirillova.gymcrmsystem.UserTestData.getNewUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void create() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(USER_1_ID + 8);
            return user;
        });

        when(traineeDAO.save(any(Trainee.class))).thenAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(TRAINEE_1_ID + 4);
            return trainee;
        });

        when(userDAO.findUsernamesByFirstNameAndLastName(any(String.class), any(String.class))).thenAnswer(invocation -> {
            String firstName = invocation.getArgument(0);
            String lastName = invocation.getArgument(1);
            return USER_LIST.stream()
                    .filter(u -> u.getFirstName().equals(firstName) && u.getLastName().equals(lastName))
                    .map(User::getUsername)
                    .sorted()
                    .toList();
        });

        User newUser = getNewUser();
        Trainee newTrainee = getNewTrainee();
        Trainee savedTrainee = traineeService.create(newUser.getFirstName(), newUser.getLastName(), newTrainee.getDateOfBirth(), newTrainee.getAddress());

        verify(userDAO, times(1)).save(any(User.class));
        verify(traineeDAO, times(1)).save(any(Trainee.class));

        int traineeId = savedTrainee.getId();
        newTrainee.setId(traineeId);

        TRAINEE_MATCHER.assertMatch(savedTrainee, newTrainee);
    }

    @Test
    void get() {
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(TRAINEE_1);
        TRAINEE_MATCHER.assertMatch(traineeService.get(TRAINEE_1_ID), TRAINEE_1);
    }

    @Test
    void delete() {
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(TRAINEE_1);

        traineeService.delete(TRAINEE_1_ID);

        verify(traineeDAO, times(1)).get(TRAINEE_1_ID);
        verify(userDAO, times(1)).delete(USER_1_ID);
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(null);

        Assertions.assertNull(traineeService.get(TRAINEE_1_ID));
    }

    @Test
    void update() {
        Trainee trainee = getUpdatedTrainee();
        User user = trainee.getUser();

        when(traineeDAO.get(trainee.getId())).thenReturn(trainee);
        when(userDAO.get(user.getId())).thenReturn(user);

        traineeService.update(trainee.getId(), user.getFirstName(), user.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(), user.isActive());

        verify(userDAO, times(1)).update(user);
        verify(traineeDAO, times(1)).update(trainee);

        when(traineeDAO.get(trainee.getId())).thenReturn(trainee);

        TRAINEE_MATCHER.assertMatch(traineeService.get(TRAINEE_1_ID), trainee);
    }

    @Test
    void getByUsername() {
        User user = TRAINEE_1.getUser();
        when(userDAO.getByUsername(user.getUsername())).thenReturn(user);
        when(traineeDAO.getByUserId(user.getId())).thenReturn(TRAINEE_1);
        TRAINEE_MATCHER.assertMatch(traineeService.getByUsername(user.getUsername()), TRAINEE_1);
    }

    @Test
    void changePassword() {
        User user = TRAINEE_1.getUser();
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(TRAINEE_1);
        when(userDAO.changePassword(user.getId(), "newPassword")).thenReturn(true);
        Assertions.assertTrue(traineeService.changePassword(TRAINEE_1_ID, "newPassword"));
    }

    @Test
    void active() {
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(TRAINEE_1);
        when(userDAO.active(TRAINEE_1.getUser().getId(), false)).thenReturn(true);
        Assertions.assertTrue(traineeService.active(TRAINEE_1_ID, false));
    }

    @Test
    void deleteByUsername() {
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(TRAINEE_1);

        traineeService.deleteByUsername(USER_1.getUsername());

        verify(userDAO, times(1)).deleteByUsername(USER_1.getUsername());
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(null);

        Assertions.assertNull(traineeService.get(TRAINEE_1_ID));
    }

    @Test
    void getFreeTrainersForTrainee() {
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_3);
        when(trainerDAO.getFreeTrainersForUsername(USER_1.getUsername())).thenReturn(expected);
        List<Trainer> actual = traineeService.getFreeTrainersForTrainee(USER_1.getUsername());
        TRAINER_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getWithTrainers() {
        when(traineeDAO.get(TRAINEE_1_ID)).thenReturn(TRAINEE_1);
        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_4);
        when(trainerDAO.getTrainersForTrainee(TRAINEE_1_ID)).thenReturn(expected);
        List<Trainer> actual = traineeService.getWithTrainers(TRAINEE_1_ID).getTrainerList();
        TRAINER_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getTrainings() {
        List<Training> expected = List.of(TRAINING_2);
        when(trainingDAO.getTraineeTrainings(
                TRAINEE_1.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINING_TYPE_2.getName(),
                TRAINER_2.getUser().getFirstName(),
                TRAINER_2.getUser().getLastName()))
                .thenReturn(expected);

        List<Training> actual = traineeService.getTrainings(
                TRAINEE_1.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINING_TYPE_2.getName(),
                TRAINER_2.getUser().getFirstName(),
                TRAINER_2.getUser().getLastName());

        TRAINING_MATCHER.assertMatch(expected, actual);
    }
}
