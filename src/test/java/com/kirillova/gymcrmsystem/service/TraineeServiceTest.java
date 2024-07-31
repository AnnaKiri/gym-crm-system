package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.error.IllegalRequestDataException;
import com.kirillova.gymcrmsystem.error.NotFoundException;
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
import static com.kirillova.gymcrmsystem.TraineeTestData.checkTraineeUserId;
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
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_LIST;
import static com.kirillova.gymcrmsystem.UserTestData.getNewUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
            user.setId(USER_1.getId() + 8);
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
        checkTraineeUserId(newTrainee, savedTrainee);
    }

    @Test
    void get() {
        User user = TRAINEE_1.getUser();

        when(traineeDAO.get(user.getUsername())).thenReturn(TRAINEE_1);

        Trainee trainee = traineeService.get(user.getUsername());

        TRAINEE_MATCHER.assertMatch(trainee, TRAINEE_1);
        checkTraineeUserId(TRAINEE_1, trainee);
    }

    @Test
    void delete() {
        User user = TRAINEE_1.getUser();
        String username = user.getUsername();
        when(traineeDAO.get(USER_1_USERNAME)).thenThrow(new NotFoundException("Not found entity with " + username));
        traineeService.delete(username);

        verify(userDAO, times(1)).delete(username);


        assertThrows(NotFoundException.class, () -> traineeDAO.get(username));
    }

    @Test
    void update() {
        Trainee trainee = getUpdatedTrainee();
        User user = trainee.getUser();

        when(traineeDAO.get(user.getUsername())).thenReturn(trainee);
        when(userDAO.get(user.getUsername())).thenReturn(user);

        traineeService.update(user.getUsername(), user.getFirstName(), user.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(), user.isActive());

        verify(userDAO, times(1)).update(user);
        verify(traineeDAO, times(1)).update(trainee);

        when(traineeDAO.get(user.getUsername())).thenReturn(trainee);

        Trainee traineeGet = traineeService.get(user.getUsername());

        TRAINEE_MATCHER.assertMatch(traineeGet, trainee);
        checkTraineeUserId(trainee, traineeGet);
    }

    @Test
    void changePassword() {
        User user = TRAINEE_1.getUser();

        when(userDAO.changePassword(user.getUsername(), "newPassword")).thenReturn(true);
        Assertions.assertTrue(traineeService.changePassword(user.getUsername(), "newPassword"));
    }

    @Test
    void active() {
        User user = TRAINEE_1.getUser();

        when(userDAO.getActive(user.getUsername())).thenReturn(true);
        when(userDAO.setActive(user.getUsername(), false)).thenReturn(true);

        Assertions.assertTrue(traineeService.setActive(user.getUsername(), false));
    }

    @Test
    void getFreeTrainersForTrainee() {
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_3);
        when(trainerDAO.getFreeTrainersForTrainee(USER_1_USERNAME)).thenReturn(expected);
        List<Trainer> actual = traineeService.getFreeTrainersForTrainee(USER_1_USERNAME);
        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getUser().getId(), actual.get(i).getUser().getId());
            Assertions.assertEquals(expected.get(i).getSpecialization().getId(), actual.get(i).getSpecialization().getId());
        }
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
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getTrainee().getId(), actual.get(i).getTrainee().getId());
            Assertions.assertEquals(expected.get(i).getTrainer().getId(), actual.get(i).getTrainer().getId());
            Assertions.assertEquals(expected.get(i).getType().getId(), actual.get(i).getType().getId());
        }
    }

    @Test
    void updateTrainerList() {
        List<String> trainerUsernames = List.of(USER_5_USERNAME);

        when(traineeDAO.get(USER_1_USERNAME)).thenReturn(TRAINEE_1);
        when(trainerDAO.get(USER_5_USERNAME)).thenReturn(TRAINER_1);
        doNothing().when(traineeDAO).updateTrainerList(USER_1_USERNAME, TRAINEE_1);

        traineeService.updateTrainerList(USER_1_USERNAME, trainerUsernames);

        verify(traineeDAO, times(1)).updateTrainerList(USER_1_USERNAME, TRAINEE_1);

        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_4);
        when(trainerDAO.getTrainersForTrainee(USER_1_USERNAME)).thenReturn(expected);

        List<Trainer> actual = trainerDAO.getTrainersForTrainee(USER_1_USERNAME);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getUser().getId(), actual.get(i).getUser().getId());
            Assertions.assertEquals(expected.get(i).getSpecialization().getId(), actual.get(i).getSpecialization().getId());
        }
    }

    @Test
    void setActiveAgain() {
        User user = TRAINEE_1.getUser();

        when(userDAO.getActive(user.getUsername())).thenReturn(true);

        Assertions.assertThrows(IllegalRequestDataException.class, () -> traineeService.setActive(user.getUsername(), true));
    }
}
