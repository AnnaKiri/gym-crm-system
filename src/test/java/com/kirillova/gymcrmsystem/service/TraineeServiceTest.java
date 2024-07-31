package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.error.IllegalRequestDataException;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.repository.TraineeRepository;
import com.kirillova.gymcrmsystem.repository.TrainerRepository;
import com.kirillova.gymcrmsystem.repository.TrainingRepository;
import com.kirillova.gymcrmsystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void create() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(USER_1.getId() + 8);
            return user;
        });

        when(traineeRepository.save(any(Trainee.class))).thenAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(TRAINEE_1_ID + 4);
            return trainee;
        });

        when(userRepository.findUsernamesByFirstNameAndLastName(any(String.class), any(String.class))).thenAnswer(invocation -> {
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

        verify(userRepository, times(1)).save(any(User.class));
        verify(traineeRepository, times(1)).save(any(Trainee.class));

        int traineeId = savedTrainee.getId();
        newTrainee.setId(traineeId);

        TRAINEE_MATCHER.assertMatch(savedTrainee, newTrainee);
        checkTraineeUserId(newTrainee, savedTrainee);
    }

    @Test
    void get() {
        when(traineeRepository.getExisted(USER_1_USERNAME)).thenReturn(TRAINEE_1);

        Trainee trainee = traineeService.get(USER_1_USERNAME);

        TRAINEE_MATCHER.assertMatch(trainee, TRAINEE_1);
        checkTraineeUserId(TRAINEE_1, trainee);
    }

    @Test
    void delete() {
        when(userRepository.deleteByUsername(USER_1_USERNAME)).thenReturn(1);

        traineeService.delete(USER_1_USERNAME);

        verify(userRepository, times(1)).deleteByUsername(USER_1_USERNAME);

        when(traineeRepository.findByUsername(USER_1_USERNAME)).thenThrow(new NotFoundException("Not found entity with " + USER_1_USERNAME));
        assertThrows(NotFoundException.class, () -> traineeRepository.findByUsername(USER_1_USERNAME));
    }

    @Test
    void update() {
        Trainee trainee = getUpdatedTrainee();
        User user = trainee.getUser();

        when(traineeRepository.getExisted(user.getUsername())).thenReturn(trainee);
        when(userRepository.getExisted(user.getUsername())).thenReturn(user);

        traineeService.update(user.getUsername(), user.getFirstName(), user.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(), user.isActive());

        verify(userRepository, times(1)).save(user);
        verify(traineeRepository, times(1)).save(trainee);

        when(traineeService.get(user.getUsername())).thenReturn(trainee);

        Trainee traineeGet = traineeService.get(user.getUsername());

        TRAINEE_MATCHER.assertMatch(traineeGet, trainee);
        checkTraineeUserId(trainee, traineeGet);
    }

    @Test
    void changePassword() {
        when(userRepository.changePassword(USER_1_USERNAME, "newPassword")).thenReturn(1);
        Assertions.assertTrue(traineeService.changePassword(USER_1_USERNAME, "newPassword"));
    }

    @Test
    void active() {
        when(userRepository.findIsActiveByUsername(USER_1_USERNAME)).thenReturn(true);
        when(userRepository.updateIsActiveByUsername(USER_1_USERNAME, false)).thenReturn(1);

        Assertions.assertFalse(traineeService.setActive(USER_1_USERNAME, false));
    }

    @Test
    void getFreeTrainersForTrainee() {
        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_3);

        ArgumentCaptor<Specification<Trainer>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        when(trainerRepository.findAll(specCaptor.capture())).thenReturn(expected);

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

        ArgumentCaptor<Specification<Training>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        when(trainingRepository.findAll(specCaptor.capture())).thenReturn(expected);

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

        when(traineeRepository.getExisted(USER_1_USERNAME)).thenReturn(TRAINEE_1);
        when(trainerRepository.getExisted(USER_5_USERNAME)).thenReturn(TRAINER_1);
        when(traineeRepository.save(TRAINEE_1)).thenReturn(TRAINEE_1);

        traineeService.updateTrainerList(USER_1_USERNAME, trainerUsernames);

        verify(traineeRepository, times(1)).save(TRAINEE_1);

        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_4);
        when(trainerRepository.findTrainersByTraineeUsername(USER_1_USERNAME)).thenReturn(expected);

        List<Trainer> actual = trainerRepository.findTrainersByTraineeUsername(USER_1_USERNAME);

        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getUser().getId(), actual.get(i).getUser().getId());
            Assertions.assertEquals(expected.get(i).getSpecialization().getId(), actual.get(i).getSpecialization().getId());
        }
    }

    @Test
    void setActiveAgain() {
        when(userRepository.findIsActiveByUsername(USER_1_USERNAME)).thenReturn(true);
        Assertions.assertThrows(IllegalRequestDataException.class, () -> traineeService.setActive(USER_1_USERNAME, true));
    }
}
