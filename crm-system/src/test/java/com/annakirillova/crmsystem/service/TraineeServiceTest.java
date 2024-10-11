package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.exception.IllegalRequestDataException;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.repository.TraineeRepository;
import com.annakirillova.crmsystem.repository.TrainerRepository;
import com.annakirillova.crmsystem.repository.TrainingRepository;
import com.annakirillova.crmsystem.repository.UserRepository;
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
import java.util.Optional;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1_ID;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_MATCHER;
import static com.annakirillova.crmsystem.TraineeTestData.checkTraineeUserId;
import static com.annakirillova.crmsystem.TraineeTestData.getNewTrainee;
import static com.annakirillova.crmsystem.TraineeTestData.getUpdatedTrainee;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_3;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_2;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.annakirillova.crmsystem.UserTestData.NOT_FOUND_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_5_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_6;
import static com.annakirillova.crmsystem.UserTestData.USER_LIST;
import static com.annakirillova.crmsystem.UserTestData.getNewUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private MessageSenderService messageSenderService;

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

        doNothing().when(keycloakService).registerUser(any(String.class), any(String.class), any(String.class), any(String.class));

        User newUser = getNewUser();
        Trainee newTrainee = getNewTrainee();
        Trainee savedTrainee = traineeService.create(newUser.getFirstName(), newUser.getLastName(), newTrainee.getDateOfBirth(), newTrainee.getAddress(), "password");

        verify(userRepository, times(1)).save(any(User.class));
        verify(traineeRepository, times(1)).save(any(Trainee.class));

        int traineeId = savedTrainee.getId();
        newTrainee.setId(traineeId);

        TRAINEE_MATCHER.assertMatch(savedTrainee, newTrainee);
        checkTraineeUserId(newTrainee, savedTrainee);
    }

    @Test
    void get() {
        when(traineeRepository.getTraineeIfExists(USER_1_USERNAME)).thenReturn(TRAINEE_1);

        Trainee trainee = traineeService.get(USER_1_USERNAME);

        TRAINEE_MATCHER.assertMatch(trainee, TRAINEE_1);
        checkTraineeUserId(TRAINEE_1, trainee);
    }

    @Test
    void delete() {
        when(userRepository.deleteByUsername(USER_1_USERNAME)).thenReturn(1);
        when(trainingRepository.findAllWithDetails(any(Specification.class))).thenReturn(List.of(TRAINING_1));
        doNothing().when(messageSenderService).sendMessage(anyString(), any(TrainingInfoDto.class));
        traineeService.delete(USER_1_USERNAME);

        verify(userRepository, times(1)).deleteByUsername(USER_1_USERNAME);
        verify(messageSenderService, times(1)).sendMessage(anyString(), any(TrainingInfoDto.class));

        when(traineeRepository.findByUsername(USER_1_USERNAME)).thenThrow(new NotFoundException("Not found entity with " + USER_1_USERNAME));
        assertThrows(NotFoundException.class, () -> traineeRepository.findByUsername(USER_1_USERNAME));
    }

    @Test
    void deleteTraineeWithWrongUsername() {
        when(userRepository.deleteByUsername(NOT_FOUND_USERNAME)).thenReturn(0);
        assertThrows(NotFoundException.class, () -> traineeService.delete(NOT_FOUND_USERNAME));

    }

    @Test
    void update() {
        Trainee trainee = getUpdatedTrainee();
        User user = trainee.getUser();

        when(traineeRepository.getTraineeIfExists(user.getUsername())).thenReturn(trainee);
        when(userRepository.getUserIfExists(user.getUsername())).thenReturn(user);

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
        doNothing().when(keycloakService).updatePassword(USER_1_USERNAME, "newPassword");

        traineeService.changePassword(USER_1_USERNAME, "newPassword");

        verify(keycloakService, times(1)).updatePassword(USER_1_USERNAME, "newPassword");
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
        when(trainingRepository.findAllWithDetails(specCaptor.capture())).thenReturn(expected);

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
        List<String> trainerUsernames = List.of(USER_5.getUsername(), USER_6.getUsername());

        when(traineeRepository.findByUsernameWithTrainerList(USER_1_USERNAME)).thenReturn(Optional.of(TRAINEE_1));
        when(trainerRepository.getTrainerIfExists(USER_5_USERNAME)).thenReturn(TRAINER_1);
        when(traineeRepository.save(TRAINEE_1)).thenReturn(TRAINEE_1);

        traineeService.updateTrainerList(USER_1_USERNAME, trainerUsernames);

        verify(traineeRepository, times(1)).save(TRAINEE_1);

        List<Trainer> expected = Arrays.asList(TRAINER_1, TRAINER_2);
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
