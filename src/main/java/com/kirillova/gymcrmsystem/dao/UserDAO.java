package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDAO {

    private final Map<Long, User> userStorage;
    private final AtomicLong index = new AtomicLong(0L);

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

