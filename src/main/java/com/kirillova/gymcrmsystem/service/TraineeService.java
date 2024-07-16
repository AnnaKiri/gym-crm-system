package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeDAO traineeDAO;
    private final UserDAO userDAO;

    public Trainee get(int traineeId) {
        log.debug("Get trainee with trainerId = " + traineeId);
        return traineeDAO.get(traineeId);
    }

    @Transactional
    public void delete(int traineeId) {
        log.debug("Delete trainee with traineeId = " + traineeId);
        Trainee trainee = get(traineeId);
        traineeDAO.delete(traineeId);
        userDAO.delete(trainee.getUser().getId());
    }

    @Transactional
    public void update(int traineeId, String firstName, String lastName, Date birthday, String address, boolean isActive) {
        log.debug("Update trainee with traineeId = " + traineeId);
        Trainee updatedTrainee = traineeDAO.get(traineeId);
        User updatedUser = userDAO.get(updatedTrainee.getUser().getId());

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        userDAO.update(updatedUser);

        updatedTrainee.setDateOfBirth(birthday);
        updatedTrainee.setAddress(address);
        traineeDAO.update(updatedTrainee);
    }

    @Transactional
    public Trainee create(String firstName, String lastName, Date birthday, String address) {
        log.debug("Create new user");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(UserUtil.generateUsername(firstName, lastName, userDAO.findUsernamesByFirstNameAndLastName(firstName, lastName)));
        newUser.setPassword(UserUtil.generatePassword());
        newUser.setActive(true);
        newUser = userDAO.save(newUser);

        log.debug("Create new trainee");
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(birthday);
        trainee.setUser(newUser);
        return traineeDAO.save(trainee);
    }
}
