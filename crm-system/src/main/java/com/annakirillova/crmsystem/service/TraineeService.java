package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.ActionType;
import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.exception.IllegalRequestDataException;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.repository.TraineeRepository;
import com.annakirillova.crmsystem.repository.TrainerRepository;
import com.annakirillova.crmsystem.repository.TrainerSpecifications;
import com.annakirillova.crmsystem.repository.TrainingRepository;
import com.annakirillova.crmsystem.repository.TrainingSpecifications;
import com.annakirillova.crmsystem.repository.UserRepository;
import com.annakirillova.crmsystem.util.UserUtil;
import com.annakirillova.crmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final TrainerMessageSender trainerMessageSenderService;

    @Transactional
    public Trainee create(String firstName, String lastName, LocalDate birthday, String address, String password) {
        log.debug("Create new user");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(
                UserUtil.generateUsername(
                        firstName, lastName,
                        userRepository.findUsernamesByFirstNameAndLastName(firstName, lastName)));
        newUser.setActive(true);
        ValidationUtil.validate(newUser);
        newUser = userRepository.save(newUser);

        log.debug("Create new trainee");
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(birthday);
        trainee.setUser(newUser);
        ValidationUtil.validate(trainee);
        Trainee savedTrainee = traineeRepository.save(trainee);

        keycloakService.registerUser(savedTrainee.getUser().getUsername(), firstName, lastName, password);
        return savedTrainee;
    }

    @Transactional
    public void changePassword(String username, String newPassword) {
        log.debug("Change password for trainee with username = {}", username);
        keycloakService.updatePassword(username, newPassword);
    }

    public List<Trainee> getTraineesForTrainer(String username) {
        log.debug("Get trainees list for trainer with username = {}", username);
        return traineeRepository.findTraineesByTrainerUsername(username);
    }

    @Transactional
    public void update(String username, String firstName, String lastName, LocalDate birthday, String address, boolean isActive) {
        log.debug("Update trainee with username = {}", username);
        Trainee updatedTrainee = traineeRepository.getTraineeIfExists(username);
        User updatedUser = userRepository.getUserIfExists(username);

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        ValidationUtil.validate(updatedUser);
        userRepository.save(updatedUser);

        updatedTrainee.setDateOfBirth(birthday);
        updatedTrainee.setAddress(address);
        ValidationUtil.validate(updatedTrainee);
        traineeRepository.save(updatedTrainee);

        keycloakService.updateUser(username, firstName, lastName);
    }

    @Transactional
    public void delete(String username) {
        log.debug("Delete trainee with username = {}", username);
        List<Training> trainings = getTrainings(username, null, null, null, null, null);
        int deletedEntities = userRepository.deleteByUsername(username);
        keycloakService.deleteUser(username);

        if (deletedEntities > 0) {
            log.debug("User and related entities with username = {} deleted", username);
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }

        for (Training training : trainings) {
            TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
                    .username(training.getTrainer().getUser().getUsername())
                    .firstName(training.getTrainer().getUser().getFirstName())
                    .lastName(training.getTrainer().getUser().getLastName())
                    .isActive(training.getTrainer().getUser().isActive())
                    .date(training.getDate())
                    .duration(training.getDuration())
                    .actionType(ActionType.DELETE)
                    .build();
            trainerMessageSenderService.sendMessage(trainingInfoDto);
        }
    }

    public List<Trainer> getFreeTrainersForTrainee(String username) {
        log.debug("Get trainers list that not assigned to trainee with username = {}", username);

        List<Trainer> freeTrainers = trainerRepository.findAll(TrainerSpecifications.notAssignedToTrainee(username));

        freeTrainers.sort(Comparator.comparingLong(Trainer::getId));
        return freeTrainers;
    }

    public List<Training> getTrainings(String username, LocalDate fromDate,
                                       LocalDate toDate, String trainingType,
                                       String trainerFirstName, String trainerLastName) {
        log.debug("Get Trainings List by trainee username and criteria (from date, to date, trainer name, training type) for trainee with username = {}", username);

        Specification<Training> spec = TrainingSpecifications
                .getTraineeTrainings(username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        return trainingRepository.findAllWithDetails(spec);
    }

    @Transactional
    public boolean setActive(String username, boolean isActive) {
        log.debug("Change active status for trainee with username = {}", username);
        Boolean currentStatus = userRepository.findIsActiveByUsername(username);

        if (currentStatus == null) {
            throw new NotFoundException("Not found entity with " + username);
        }

        if (currentStatus == isActive) {
            throw new IllegalRequestDataException("User is already in the desired state");
        }

        int updatedEntities = userRepository.updateIsActiveByUsername(username, isActive);

        if (updatedEntities > 0) {
            log.debug("Status was changed for user with username = {}", username);
            return isActive;
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }
    }

    public Trainee get(String username) {
        log.debug("Get trainee with username = {}", username);
        return traineeRepository.getTraineeIfExists(username);
    }

    public Trainee getWithUser(String username) {
        log.debug("Get trainee with username = {} with user entity", username);
        return traineeRepository
                .getWithUser(username)
                .orElseThrow(() -> new NotFoundException("User with username=" + username + " not found"));
    }

    @Transactional
    public void updateTrainerList(String username, List<String> trainers) {
        log.debug("Update trainers list for trainee with username = {}", username);
        Trainee updatedTrainee = traineeRepository
                .findByUsernameWithTrainerList(username)
                .orElseThrow(() -> new NotFoundException("Trainee with username=" + username + " not found"));
        updatedTrainee.getTrainerList().clear();
        for (String trainerUsername : trainers) {
            Trainer trainer = trainerRepository.getTrainerIfExists(trainerUsername);
            updatedTrainee.getTrainerList().add(trainer);
        }
        traineeRepository.save(updatedTrainee);
    }
}
