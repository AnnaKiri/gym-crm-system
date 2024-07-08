package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserDAO {
    private final Map<Long, User> userStorage;

    @Autowired
    public UserDAO(Map<Long, User> userStorage) {
        this.userStorage = userStorage;
    }

    public User save(User user) {
        return userStorage.put(user.getId(), user);
    }

    public void update(long userId, User updatedUser) {
        userStorage.put(userId, updatedUser);
    }

    public void delete(long userId) {
        userStorage.remove(userId);
    }

    public User getUser(long userId) {
        return userStorage.get(userId);
    }
}

