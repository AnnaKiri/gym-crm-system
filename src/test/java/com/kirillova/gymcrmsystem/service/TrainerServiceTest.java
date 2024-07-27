package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.TrainingTypeDAO;
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
import java.util.Arrays;
import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1_ID;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.kirillova.gymcrmsystem.TrainerTestData.checkTrainerSpecializationId;
import static com.kirillova.gymcrmsystem.TrainerTestData.checkTrainerUserId;
import static com.kirillova.gymcrmsystem.TrainerTestData.getNewTrainer;
import static com.kirillova.gymcrmsystem.TrainerTestData.getUpdatedTrainer;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_5;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTraineeId;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTrainerId;
import static com.kirillova.gymcrmsystem.TrainingTestData.checkTrainingTypeId;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
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

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void get() {
        when(trainerDAO.get(USER_5.getUsername())).thenReturn(TRAINER_1);

        Trainer trainer = trainerService.get(USER_5.getUsername());

        TRAINER_MATCHER.assertMatch(trainer, TRAINER_1);
        checkTrainerUserId(TRAINER_1, trainer);
        checkTrainerSpecializationId(TRAINER_1, trainer);
    }

    @Test
    void update() {
        Trainer trainer = getUpdatedTrainer();
        User user = trainer.getUser();

        when(trainerDAO.get(user.getUsername())).thenReturn(trainer);
        when(userDAO.get(user.getUsername())).thenReturn(user);
        when(trainingTypeDAO.get(trainer.getSpecialization().getId())).thenReturn(trainer.getSpecialization());

        trainerService.update(user.getUsername(), user.getFirstName(), user.getLastName(), trainer.getSpecialization().getId(), user.isActive());

        verify(userDAO, times(1)).update(user);
        verify(trainerDAO, times(1)).update(trainer);

        Trainer trainerGet = trainerService.get(user.getUsername());

        TRAINER_MATCHER.assertMatch(trainerGet, trainer);
        checkTrainerUserId(trainer, trainerGet);
        checkTrainerSpecializationId(trainer, trainerGet);
    }

    @Test
    void create() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(USER_1.getId() + 8);
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

        when(trainingTypeDAO.get(any(Integer.class))).thenAnswer(invocation -> TRAINING_TYPE_4);

        User newUser = getNewUser();
        Trainer newTrainer = getNewTrainer();
        Trainer savedTrainer = trainerService.create(newUser.getFirstName(), newUser.getLastName(), newTrainer.getSpecialization().getId());

        verify(userDAO, times(1)).save(any(User.class));
        verify(trainerDAO, times(1)).save(any(Trainer.class));

        int trainerId = savedTrainer.getId();
        newTrainer.setId(trainerId);

        TRAINER_MATCHER.assertMatch(savedTrainer, newTrainer);
        checkTrainerUserId(newTrainer, savedTrainer);
        checkTrainerSpecializationId(newTrainer, savedTrainer);
    }

    @Test
    void getByUsername() {
        User user = TRAINER_1.getUser();

        when(trainerDAO.get(user.getUsername())).thenReturn(TRAINER_1);

        Trainer trainer = trainerService.get(user.getUsername());

        TRAINER_MATCHER.assertMatch(trainer, TRAINER_1);
        checkTrainerUserId(TRAINER_1, trainer);
        checkTrainerSpecializationId(TRAINER_1, trainer);
    }

    @Test
    void changePassword() {
        User user = TRAINER_1.getUser();

        when(userDAO.changePassword(user.getUsername(), "newPassword")).thenReturn(true);

        Assertions.assertTrue(trainerService.changePassword(user.getUsername(), "newPassword"));
    }

    @Test
    void active() {
        User user = TRAINER_1.getUser();

        when(userDAO.setActive(user.getUsername(), false)).thenReturn(true);

        Assertions.assertTrue(trainerService.setActive(user.getUsername(), false));
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
        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getWithTrainers() {
        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_4);
        when(trainerDAO.getTrainersForTrainee(USER_1_USERNAME)).thenReturn(expected);
        List<Trainer> actual = trainerService.getTrainersForTrainee(USER_1_USERNAME);
        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getUser().getId(), actual.get(i).getUser().getId());
            Assertions.assertEquals(expected.get(i).getSpecialization().getId(), actual.get(i).getSpecialization().getId());
        }
    }
}