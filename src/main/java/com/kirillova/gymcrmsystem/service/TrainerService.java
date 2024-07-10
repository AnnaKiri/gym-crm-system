package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.AppConfig;
import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.util.UserUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TrainerService implements InitializingBean {
    private static final Logger log = getLogger(TrainerService.class);

    private final AppConfig appConfig;

    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;
    private Set<String> allUsernames;

    @Autowired
    public TrainerService(AppConfig appConfig, TrainerDAO trainerDAO, UserDAO userDAO) {
        this.appConfig = appConfig;
        this.trainerDAO = trainerDAO;
        this.userDAO = userDAO;
    }

    @Autowired
    public void setAllUsernames(Set<String> allUsernames) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(appConfig.getTrainerDataPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); //firstName, lastName, specializationId
                create(parts[0], parts[1], Long.parseLong(parts[2]));
            }
        } catch (IOException e) {
            log.debug("Error reading trainer init data file");
        }
    }
}
