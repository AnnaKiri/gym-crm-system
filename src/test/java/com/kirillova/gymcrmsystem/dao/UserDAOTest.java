package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kirillova.gymcrmsystem.TestData.updatedUserForTrainee;
import static com.kirillova.gymcrmsystem.TestData.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOTest {

    private Map<Long, User> userStorage;
    private UserDAO userDAO;
    User testUser;
    private User savedUser;
    private long userId;

    @BeforeEach
    public void setUp() {
        userStorage = new HashMap<>();
        userDAO = new UserDAO(userStorage);
        testUser = new User(user1);
        savedUser = userDAO.save(testUser);
        userId = savedUser.getId();
    }

    @Test
    void save() {
        assertNotNull(savedUser);
        assertEquals(savedUser, user1);
        assertTrue(userStorage.containsKey(savedUser.getId()));
    }

    @Test
    void update() {
        userDAO.update(userId, updatedUserForTrainee);
        assertEquals(updatedUserForTrainee, userStorage.get(userId));
    }

    @Test
    void delete() {
        userDAO.delete(userId);
        assertFalse(userStorage.containsKey(userId));
    }

    @Test
    void getUser() {
        User retrievedUser = userDAO.getUser(userId);
        assertEquals(savedUser, retrievedUser);
    }
}