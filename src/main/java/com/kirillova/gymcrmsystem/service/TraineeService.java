package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.util.DataLoaderUtil;
import com.kirillova.gymcrmsystem.util.UserUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TraineeService implements InitializingBean {
    private static final Logger log = getLogger(TraineeService.class);

    private final ConfigurationProperties configurationProperties;

    private final TraineeDAO traineeDAO;
    private final UserDAO userDAO;
    private final Set<String> allUsernames;

    @Autowired
    public TraineeService(ConfigurationProperties configurationProperties, TraineeDAO traineeDAO, UserDAO userDAO, Set<String> allUsernames) {
        this.configurationProperties = configurationProperties;
        this.traineeDAO = traineeDAO;
        this.userDAO = userDAO;
        this.allUsernames = allUsernames;
    }

    public Trainee get(long traineeId) {
        log.debug("Get trainee with id = " + traineeId);
        return traineeDAO.getTrainee(traineeId);
    }

    public void delete(long traineeId) {
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

    @Override
    public void afterPropertiesSet() {
        DataLoaderUtil.loadData(configurationProperties.getTraineeDataPath(), parts -> {
            // firstName, lastName, Date of Birth, Address
            create(parts[0], parts[1], LocalDate.parse(parts[2]), parts[3]);
        });
    }
}
