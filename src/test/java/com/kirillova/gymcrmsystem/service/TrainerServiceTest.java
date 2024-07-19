package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
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
import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1_ID;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1_ID;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.kirillova.gymcrmsystem.TrainerTestData.getNewTrainer;
import static com.kirillova.gymcrmsystem.TrainerTestData.getUpdatedTrainer;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_5;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_ID;
import static com.kirillova.gymcrmsystem.UserTestData.USER_LIST;
import static com.kirillova.gymcrmsystem.UserTestData.getNewUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void get() {
        when(trainerDAO.get(TRAINEE_1_ID)).thenReturn(TRAINER_1);
        TRAINER_MATCHER.assertMatch(trainerService.get(TRAINER_1_ID), TRAINER_1);
    }

    @Test
    void update() {
        Trainer trainer = getUpdatedTrainer();
        User user = trainer.getUser();

        when(trainerDAO.get(trainer.getId())).thenReturn(trainer);
        when(userDAO.get(user.getId())).thenReturn(user);

        trainerService.update(trainer.getId(), user.getFirstName(), user.getLastName(), trainer.getSpecialization(), user.isActive());

        verify(userDAO, times(1)).update(user);
        verify(trainerDAO, times(1)).update(trainer);

        when(trainerDAO.get(trainer.getId())).thenReturn(trainer);

        TRAINER_MATCHER.assertMatch(trainerService.get(TRAINER_1_ID), trainer);
    }

    @Test
    void create() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(USER_1_ID + 8);
            return user;
        });

        when(trainerDAO.save(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(TRAINER_1_ID + 4);
            return trainer;
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
        Trainer newTrainer = getNewTrainer();
        Trainer savedTrainer = trainerService.create(newUser.getFirstName(), newUser.getLastName(), newTrainer.getSpecialization());

        verify(userDAO, times(1)).save(any(User.class));
        verify(trainerDAO, times(1)).save(any(Trainer.class));

        int trainerId = savedTrainer.getId();
        newTrainer.setId(trainerId);

        TRAINER_MATCHER.assertMatch(savedTrainer, newTrainer);
    }

    @Test
    void getByUsername() {
        User user = TRAINER_1.getUser();
        when(userDAO.getByUsername(user.getUsername())).thenReturn(user);
        when(trainerDAO.getByUserId(user.getId())).thenReturn(TRAINER_1);
        TRAINER_MATCHER.assertMatch(trainerService.getByUsername(user.getUsername()), TRAINER_1);
    }

    @Test
    void changePassword() {
        User user = TRAINER_1.getUser();
        when(trainerDAO.get(TRAINER_1_ID)).thenReturn(TRAINER_1);
        when(userDAO.changePassword(user.getId(), "newPassword")).thenReturn(true);
        Assertions.assertTrue(trainerService.changePassword(TRAINER_1_ID, "newPassword"));
    }

    @Test
    void active() {
        when(trainerDAO.get(TRAINER_1_ID)).thenReturn(TRAINER_1);
        when(userDAO.active(TRAINER_1.getUser().getId(), false)).thenReturn(true);
        Assertions.assertTrue(trainerService.active(TRAINER_1_ID, false));
    }

    @Test
    void getTrainings() {
        List<Training> expected = List.of(TRAINING_5);
        when(trainingDAO.getTrainerTrainings(
                TRAINER_1.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINEE_3.getUser().getFirstName(),
                TRAINEE_3.getUser().getLastName()))
                .thenReturn(expected);

        List<Training> actual = trainerService.getTrainings(
                TRAINER_1.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINEE_3.getUser().getFirstName(),
                TRAINEE_3.getUser().getLastName());

        TRAINING_MATCHER.assertMatch(expected, actual);
    }
}