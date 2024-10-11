package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.exception.IllegalRequestDataException;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.repository.TrainerRepository;
import com.annakirillova.crmsystem.repository.TrainingRepository;
import com.annakirillova.crmsystem.repository.TrainingSpecifications;
import com.annakirillova.crmsystem.repository.TrainingTypeRepository;
import com.annakirillova.crmsystem.repository.UserRepository;
import com.annakirillova.crmsystem.util.UserUtil;
import com.annakirillova.crmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final KeycloakService keycloakService;

    @Transactional
    public Trainer create(String firstName, String lastName, Integer specializationId, String password) {
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

        log.debug("Create new trainer");
        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingTypeRepository.getTrainingTypeIfExists(specializationId));
        trainer.setUser(newUser);
        ValidationUtil.validate(trainer);
        Trainer savedTrainer = trainerRepository.save(trainer);

        keycloakService.registerUser(savedTrainer.getUser().getUsername(), firstName, lastName, password);

        return savedTrainer;
    }

    @Transactional
    public void changePassword(String username, String newPassword) {
        log.debug("Change password for trainer with username = {}", username);
        keycloakService.updatePassword(username, newPassword);
    }

    public List<Trainer> getTrainersForTrainee(String username) {
        log.debug("Get trainers list for trainee with username = {}", username);
        return trainerRepository.findTrainersByTraineeUsername(username);
    }

    @Transactional
    public void update(String username, String firstName, String lastName, Integer specializationId, boolean isActive) {
        log.debug("Update trainer with username = {}", username);
        Trainer updatedTrainer = trainerRepository.getTrainerIfExists(username);
        User updatedUser = userRepository.getUserIfExists(username);

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        ValidationUtil.validate(updatedUser);
        userRepository.save(updatedUser);

        updatedTrainer.setSpecialization(trainingTypeRepository.getTrainingTypeIfExists(specializationId));
        ValidationUtil.validate(updatedTrainer);
        trainerRepository.save(updatedTrainer);

        keycloakService.updateUser(username, firstName, lastName);
    }

    public List<Training> getTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeFirstName, String traineeLastName) {
        log.debug("Get Trainings List by trainer username and criteria (from date, to date, trainee name) for trainer with username = {}", username);
        Specification<Training> spec = TrainingSpecifications
                .getTrainerTrainings(username, fromDate, toDate, traineeFirstName, traineeLastName);
        return trainingRepository.findAllWithDetails(spec);
    }

    @Transactional
    public boolean setActive(String username, boolean isActive) {
        log.debug("Change active status for trainer with username = {}", username);
        boolean currentStatus = userRepository.findIsActiveByUsername(username);
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

    public Trainer get(String username) {
        log.debug("Get trainer with username = {}", username);
        return trainerRepository.getTrainerIfExists(username);
    }

    public Trainer getWithUserAndSpecialization(String username) {
        log.debug("Get trainer with username = {} with user and specialization entity", username);
        return trainerRepository
                .getWithUserAndSpecialization(username)
                .orElseThrow(() -> new NotFoundException("User with username=" + username + " not found"));
    }

}
