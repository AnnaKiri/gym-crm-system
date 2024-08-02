package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.error.IllegalRequestDataException;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.repository.TrainerRepository;
import com.kirillova.gymcrmsystem.repository.TrainingRepository;
import com.kirillova.gymcrmsystem.repository.TrainingTypeRepository;
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
import static com.kirillova.gymcrmsystem.UserTestData.USER_5_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_LIST;
import static com.kirillova.gymcrmsystem.UserTestData.getNewUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void get() {
        when(trainerRepository.getExisted(USER_5_USERNAME)).thenReturn(TRAINER_1);

        Trainer trainer = trainerService.get(USER_5.getUsername());

        TRAINER_MATCHER.assertMatch(trainer, TRAINER_1);
        checkTrainerUserId(TRAINER_1, trainer);
        checkTrainerSpecializationId(TRAINER_1, trainer);
    }

    @Test
    void update() {
        Trainer trainer = getUpdatedTrainer();
        User user = trainer.getUser();

        when(trainerRepository.getExisted(user.getUsername())).thenReturn(trainer);
        when(userRepository.getExisted(user.getUsername())).thenReturn(user);
        when(trainingTypeRepository.getExisted(trainer.getSpecialization().getId())).thenReturn(trainer.getSpecialization());

        trainerService.update(user.getUsername(), user.getFirstName(), user.getLastName(), trainer.getSpecialization().getId(), user.isActive());

        verify(userRepository, times(1)).save(user);
        verify(trainerRepository, times(1)).save(trainer);

        Trainer trainerGet = trainerService.get(user.getUsername());

        TRAINER_MATCHER.assertMatch(trainerGet, trainer);
        checkTrainerUserId(trainer, trainerGet);
        checkTrainerSpecializationId(trainer, trainerGet);
    }

    @Test
    void create() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(USER_1.getId() + 8);
            return user;
        });

        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(TRAINER_1_ID + 4);
            return trainer;
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

        when(trainingTypeRepository.getExisted(any(Integer.class))).thenAnswer(invocation -> TRAINING_TYPE_4);

        User newUser = getNewUser();
        Trainer newTrainer = getNewTrainer();
        Trainer savedTrainer = trainerService.create(newUser.getFirstName(), newUser.getLastName(), newTrainer.getSpecialization().getId());

        verify(userRepository, times(1)).save(any(User.class));
        verify(trainerRepository, times(1)).save(any(Trainer.class));

        int trainerId = savedTrainer.getId();
        newTrainer.setId(trainerId);

        TRAINER_MATCHER.assertMatch(savedTrainer, newTrainer);
        checkTrainerUserId(newTrainer, savedTrainer);
        checkTrainerSpecializationId(newTrainer, savedTrainer);
    }

    @Test
    void getByUsername() {
        when(trainerRepository.getExisted(USER_5_USERNAME)).thenReturn(TRAINER_1);

        Trainer trainer = trainerService.get(USER_5_USERNAME);

        TRAINER_MATCHER.assertMatch(trainer, TRAINER_1);
        checkTrainerUserId(TRAINER_1, trainer);
        checkTrainerSpecializationId(TRAINER_1, trainer);
    }

    @Test
    void changePassword() {
        when(userRepository.changePassword(USER_5_USERNAME, "newPassword")).thenReturn(1);
        Assertions.assertTrue(trainerService.changePassword(USER_5_USERNAME, "newPassword"));
    }

    @Test
    void active() {
        when(userRepository.findIsActiveByUsername(USER_5_USERNAME)).thenReturn(true);
        when(userRepository.updateIsActiveByUsername(USER_5_USERNAME, false)).thenReturn(1);

        Assertions.assertFalse(trainerService.setActive(USER_5_USERNAME, false));
    }

    @Test
    void getTrainings() {
        List<Training> expected = List.of(TRAINING_5);

        ArgumentCaptor<Specification<Training>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        when(trainingRepository.findAllWithDetails(specCaptor.capture())).thenReturn(expected);

        List<Training> actual = trainerService.getTrainings(
                TRAINER_1.getUser().getUsername(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 15),
                TRAINEE_3.getUser().getFirstName(),
                TRAINEE_3.getUser().getLastName());

        TRAINING_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            checkTrainingTraineeId(expected.get(i), actual.get(i));
            checkTrainingTrainerId(expected.get(i), actual.get(i));
            checkTrainingTypeId(expected.get(i), actual.get(i));
        }
    }

    @Test
    void getWithTrainers() {
        List<Trainer> expected = Arrays.asList(TRAINER_2, TRAINER_4);
        when(trainerRepository.findTrainersByTraineeUsername(USER_1_USERNAME)).thenReturn(expected);
        List<Trainer> actual = trainerService.getTrainersForTrainee(USER_1_USERNAME);
        TRAINER_MATCHER.assertMatch(actual, expected);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getUser().getId(), actual.get(i).getUser().getId());
            Assertions.assertEquals(expected.get(i).getSpecialization().getId(), actual.get(i).getSpecialization().getId());
        }
    }

    @Test
    void setActiveAgain() {
        when(userRepository.findIsActiveByUsername(USER_5_USERNAME)).thenReturn(true);
        Assertions.assertThrows(IllegalRequestDataException.class, () -> trainerService.setActive(USER_5_USERNAME, true));
    }
}