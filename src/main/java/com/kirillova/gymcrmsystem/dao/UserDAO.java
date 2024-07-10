package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserDAO {
    private final Map<Long, User> userStorage;
    private final AtomicLong index = new AtomicLong(1L);

    @Autowired
    public UserDAO(Map<Long, User> userStorage) {
        this.userStorage = userStorage;
    }

    public User save(User user) {
        long newId = index.incrementAndGet();
        user.setId(newId);
        userStorage.put(newId, user);
        return user;
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

