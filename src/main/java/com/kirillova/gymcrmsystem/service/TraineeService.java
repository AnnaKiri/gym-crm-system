package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.util.UserUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TraineeService {
    private static final Logger log = getLogger(TraineeService.class);

    private final TraineeDAO traineeDAO;
    private final UserDAO userDAO;
    private Set<String> allUsernames;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO, UserDAO userDAO) {
        this.traineeDAO = traineeDAO;
        this.userDAO = userDAO;
    }

    @Autowired
    public void setAllUsernames(Set<String> allUsernames) {
        this.allUsernames = allUsernames;
    }

    public Trainee get(int traineeId) {
        log.debug("Get trainee with id = " + traineeId);
        return traineeDAO.getTrainee(traineeId);
    }

    public void delete(int traineeId) {
        log.debug("Delete trainee with userId = " + traineeId);
        Trainee trainee = get(traineeId);
        traineeDAO.delete(traineeId);
        userDAO.delete(trainee.getUserId());
    }

    public void update(long traineeId, String firstName, String lastName, LocalDate birthday, String address, boolean isActive) {
        log.debug("Update trainee with userId = " + traineeId);
        Trainee updatedTrainee = traineeDAO.getTrainee(traineeId);
        User updatedUser = userDAO.getUser(updatedTrainee.getUserId());

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        userDAO.update(updatedUser.getId(), updatedUser);

        updatedTrainee.setDateOfBirth(birthday);
        updatedTrainee.setAddress(address);
        traineeDAO.update(traineeId, updatedTrainee);
    }

    public Trainee create(String firstName, String lastName, LocalDate birthday, String address) {
        log.debug("Create new user");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(UserUtil.generateUsername(firstName, lastName, allUsernames));
        newUser.setPassword(UserUtil.generatePassword());
        newUser.setActive(true);
        newUser = userDAO.save(newUser);

        log.debug("Create new trainee");
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(birthday);
        trainee.setUserId(newUser.getId());
        return traineeDAO.save(trainee);
    }
}
