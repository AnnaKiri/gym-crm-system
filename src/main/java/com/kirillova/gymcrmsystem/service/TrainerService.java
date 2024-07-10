package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.util.DataLoaderUtil;
import com.kirillova.gymcrmsystem.util.UserUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TrainerService implements InitializingBean {
    private static final Logger log = getLogger(TrainerService.class);

    private final ConfigurationProperties configurationProperties;

    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;
    private final Set<String> allUsernames;

    @Autowired
    public TrainerService(ConfigurationProperties configurationProperties, TrainerDAO trainerDAO, UserDAO userDAO, Set<String> allUsernames) {
        this.configurationProperties = configurationProperties;
        this.trainerDAO = trainerDAO;
        this.userDAO = userDAO;
        this.allUsernames = allUsernames;
    }

    public Trainer get(long trainerId) {
        log.debug("Get trainer with id = " + trainerId);
        return trainerDAO.getTrainer(trainerId);
    }

    public void update(long trainerId, String firstName, String lastName, long specializationId, boolean isActive) {
        log.debug("Update trainer with userId = " + trainerId);
        Trainer updatedTrainer = trainerDAO.getTrainer(trainerId);
        User updatedUser = userDAO.getUser(updatedTrainer.getUserId());

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setActive(isActive);
        userDAO.update(updatedUser.getId(), updatedUser);

        updatedTrainer.setSpecialization(specializationId);
        trainerDAO.update(trainerId, updatedTrainer);
    }

    public Trainer create(String firstName, String lastName, long specializationId) {
        log.debug("Create new user");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(UserUtil.generateUsername(firstName, lastName, allUsernames));
        newUser.setPassword(UserUtil.generatePassword());
        newUser.setActive(true);
        newUser = userDAO.save(newUser);

        log.debug("Create new trainer");
        Trainer trainer = new Trainer();
        trainer.setSpecialization(specializationId);
        trainer.setUserId(newUser.getId());
        return trainerDAO.save(trainer);
    }

    @Override
    public void afterPropertiesSet() {
        DataLoaderUtil.loadData(configurationProperties.getTrainerDataPath(), parts -> {
            // firstName, lastName, specializationId
            create(parts[0], parts[1], Long.parseLong(parts[2]));
        });
    }
}
