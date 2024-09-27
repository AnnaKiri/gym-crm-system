package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.error.DataConflictException;
import com.annakirillova.crmsystem.error.IllegalRequestDataException;
import com.annakirillova.crmsystem.error.NotFoundException;
import com.annakirillova.crmsystem.feign.TrainerWorkloadServiceFeignClient;
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
import com.annakirillova.crmsystem.security.JWTProvider;
import com.annakirillova.crmsystem.util.UserUtil;
import com.annakirillova.crmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static com.annakirillova.crmsystem.config.SecurityConfig.PASSWORD_ENCODER;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

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
        newUser.setPassword(password);
        newUser.setActive(true);
        ValidationUtil.validate(newUser);
        newUser = userRepository.prepareAndSaveWithPassword(newUser);

        log.debug("Create new trainee");
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(birthday);
        trainee.setUser(newUser);
        ValidationUtil.validate(trainee);
        return traineeRepository.save(trainee);
    }

    @Transactional
    public boolean changePassword(String username, String newPassword) {
        log.debug("Change password for trainee with username = {}", username);
        int updatedEntities = userRepository.changePassword(username, PASSWORD_ENCODER.encode(newPassword));

        if (updatedEntities > 0) {
            log.debug("Changed password for user with username = {}", username);
            return true;
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }
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
    }

    @Transactional
    public void delete(String username) {
        log.debug("Delete trainee with username = {}", username);
        List<Training> trainings = getTrainings(username, null, null, null, null, null);
        int deletedEntities = userRepository.deleteByUsername(username);

        if (deletedEntities > 0) {
            log.debug("User and related entities with username = {} deleted", username);
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }

        String jwtToken = authService.getJwtToken();
        if (jwtToken == null) {
            log.error("JWT token is missing, unable to make a call to trainerWorkloadService.");
            throw new DataConflictException("JWT token is missing");
        }

        for (Training training : trainings) {
            TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
                    .username(training.getTrainer().getUser().getUsername())
                    .firstName(training.getTrainer().getUser().getFirstName())
                    .lastName(training.getTrainer().getUser().getLastName())
                    .isActive(training.getTrainer().getUser().isActive())
                    .date(training.getDate())
                    .duration(training.getDuration())
                    .actionType(TrainingInfoDto.ACTION_TYPE_DELETE)
                    .build();
            trainerWorkloadServiceFeignClient.updateTrainingInfo(JWTProvider.BEARER_PREFIX + jwtToken,
                    MDC.get("transactionId"),
                    trainingInfoDto);
        }
    }

    public List<Trainer> getFreeTrainersForTrainee(String username) {
        log.debug("Get trainers list that not assigned to trainee with username = {}", username);

        List<Trainer> freeTrainers = trainerRepository.findAll(TrainerSpecifications.notAssignedToTrainee(username));

        freeTrainers.sort(Comparator.comparingLong(Trainer::getId));
        return freeTrainers;
    }

    public List<Training> getTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainingType, String trainerFirstName, String trainerLastName) {
        log.debug("Get Trainings List by trainee username and criteria (from date, to date, trainer name, training type) for trainee with username = {}", username);

        Specification<Training> spec = TrainingSpecifications
                .getTraineeTrainings(username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        return trainingRepository.findAllWithDetails(spec);
    }

    @Transactional
    public boolean setActive(String username, boolean isActive) {
        log.debug("Change active status for trainee with username = {}", username);
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
                .orElseThrow(() -> new NotFoundException("Trainer with username=" + username + " not found"));
        updatedTrainee.getTrainerList().clear();
        for (String trainerUsername : trainers) {
            Trainer trainer = trainerRepository.getTrainerIfExists(trainerUsername);
            updatedTrainee.getTrainerList().add(trainer);
        }
        traineeRepository.save(updatedTrainee);
    }
}
