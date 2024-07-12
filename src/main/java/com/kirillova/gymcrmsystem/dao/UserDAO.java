package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.service.TraineeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class UserDAO {
    private static final Logger log = getLogger(TraineeService.class);

    private final Map<Long, User> userStorage;
    private final AtomicLong index = new AtomicLong(0L);

    @Autowired
    public UserDAO(Map<Long, User> userStorage) {
        this.userStorage = userStorage;
    }

    public User save(User user) {
        long newId = index.incrementAndGet();
        user.setId(newId);
        userStorage.put(newId, user);
        log.debug("New user with id = " + newId + " saved");
        return user;
    }

    public void update(long userId, User updatedUser) {
        userStorage.put(userId, updatedUser);
        log.debug("User with id = " + userId + " updated");
    }

    public void delete(long userId) {
        userStorage.remove(userId);
        log.debug("User with id = " + userId + " deleted");
    }

    public User getUser(long userId) {
        log.debug("Get user with id = " + userId);
        return userStorage.get(userId);
    }
}

