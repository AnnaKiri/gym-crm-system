package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.util.UserUtil;
import com.kirillova.gymcrmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final UserDAO userDAO;

    @Transactional
    public Trainer create(String firstName, String lastName, TrainingType specialization) {
        log.debug("Create new user");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(UserUtil.generateUsername(firstName, lastName, userDAO.findUsernamesByFirstNameAndLastName(firstName, lastName)));
        newUser.setPassword(UserUtil.generatePassword());
        newUser.setActive(true);
        ValidationUtil.validate(newUser);
        newUser = userDAO.save(newUser);

        log.debug("Create new trainer");
        Trainer trainer = new Trainer();
        trainer.setSpecialization(specialization);
        trainer.setUser(newUser);
        ValidationUtil.validate(trainer);
        return trainerDAO.save(trainer);
    }

    @Transactional
    public boolean changePassword(String username, String newPassword) {
        log.debug("Change password for trainer with username = {}", username);
        ValidationUtil.validatePassword(newPassword);
        return userDAO.changePassword(username, newPassword);
    }

    public List<Trainer> getTrainersForTrainee(String username) {
        log.debug("Get trainers list for trainee with username = {}", username);
        return trainerDAO.getTrainersForTrainee(username);
    }

    @Transactional
    public void update(String username, String firstName, String lastName, TrainingType specialization, boolean isActive) {
        log.debug("Update trainer with username = {}", username);
        Trainer updatedTrainer = trainerDAO.get(username);
        User updatedUser = userDAO.get(username);

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        ValidationUtil.validate(updatedUser);
        userDAO.update(updatedUser);

        updatedTrainer.setSpecialization(specialization);
        ValidationUtil.validate(updatedTrainer);
        trainerDAO.update(updatedTrainer);
    }

    public List<Training> getTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeFirstName, String traineeLastName) {
        log.debug("Get Trainings List by trainer username and criteria (from date, to date, trainee name) for trainer with username = {}", username);
        return trainingDAO.getTrainerTrainings(username, fromDate, toDate, traineeFirstName, traineeLastName);
    }

    @Transactional
    public boolean setActive(String username, boolean isActive) {
        log.debug("Change active status for trainer with username = {}", username);
        return userDAO.setActive(username, isActive);
    }

    public Trainer get(String username) {
        log.debug("Get trainer with username = {}", username);
        return trainerDAO.get(username);
    }

    public Trainer getWithUserAndSpecialization(String username) {
        log.debug("Get trainer with username = {} with user and specialization entity", username);
        return trainerDAO.getWithUserAndSpecialization(username);
    }
}
