package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.TrainingDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
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
public class TraineeService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final UserDAO userDAO;

    public Trainee get(int id) {
        log.debug("Get trainee with trainerId = " + id);
        return traineeDAO.get(id);
    }

    @Transactional
    public void delete(int id) {
        log.debug("Delete trainee with traineeId = " + id);
        Trainee trainee = traineeDAO.get(id);
        userDAO.delete(trainee.getUser().getId());
    }

    @Transactional
    public void update(int id, String firstName, String lastName, LocalDate birthday, String address, boolean isActive) {
        log.debug("Update trainee with traineeId = " + id);
        Trainee updatedTrainee = traineeDAO.get(id);
        User updatedUser = userDAO.get(updatedTrainee.getUser().getId());

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        ValidationUtil.validate(updatedUser);
        userDAO.update(updatedUser);

        updatedTrainee.setDateOfBirth(birthday);
        updatedTrainee.setAddress(address);
        ValidationUtil.validate(updatedTrainee);
        traineeDAO.update(updatedTrainee);
    }

    @Transactional
    public Trainee create(String firstName, String lastName, LocalDate birthday, String address) {
        log.debug("Create new user");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(UserUtil.generateUsername(firstName, lastName, userDAO.findUsernamesByFirstNameAndLastName(firstName, lastName)));
        newUser.setPassword(UserUtil.generatePassword());
        newUser.setActive(true);
        ValidationUtil.validate(newUser);
        newUser = userDAO.save(newUser);

        log.debug("Create new trainee");
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(birthday);
        trainee.setUser(newUser);
        ValidationUtil.validate(trainee);
        return traineeDAO.save(trainee);
    }

    public Trainee getByUsername(String username) {
        log.debug("Get user with username = " + username);
        User user = userDAO.getByUsername(username);
        return traineeDAO.getByUserId(user.getId());
    }

    @Transactional
    public boolean changePassword(int traineeId, String newPassword) {
        log.debug("Change password for trainee with id = " + traineeId);
        ValidationUtil.validatePassword(newPassword);
        Trainee trainee = traineeDAO.get(traineeId);
        return userDAO.changePassword(trainee.getUser().getId(), newPassword);
    }

    @Transactional
    public boolean setActive(int id, boolean isActive) {
        log.debug("Change active status for trainee with id = " + id);
        Trainee trainee = traineeDAO.get(id);
        return userDAO.active(trainee.getUser().getId(), isActive);
    }

    @Transactional
    public void deleteByUsername(String username) {
        log.debug("Delete trainee with username = " + username);
        userDAO.deleteByUsername(username);
    }

    public List<Trainer> getFreeTrainersForTrainee(String username) {
        log.debug("Get trainers list that not assigned on trainee by trainee's username = " + username);
        return trainerDAO.getFreeTrainersForUsername(username);
    }

    public Trainee getWithTrainers(int id) {
        log.debug("Get trainers list for trainee with id = " + id);
        Trainee trainee = traineeDAO.get(id);
        trainee.setTrainerList(trainerDAO.getTrainersForTrainee(id));
        return trainee;
    }
    public List<Training> getTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainingType, String trainerFirstName, String trainerLastName) {
        log.debug("Get Trainings List by trainee username and criteria (from date, to date, trainer name, training type) for trainee with username = " + username);
        return trainingDAO.getTraineeTrainings(username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
    }
}
