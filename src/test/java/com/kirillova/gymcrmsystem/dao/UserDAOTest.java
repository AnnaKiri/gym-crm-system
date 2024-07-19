package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_ID;
import static com.kirillova.gymcrmsystem.UserTestData.USER_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.getNewUser;
import static com.kirillova.gymcrmsystem.UserTestData.getUpdatedUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOTest extends AbstractDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Test
    void save() {
        User savedUser = userDAO.save(getNewUser());
        int userId = savedUser.getId();
        User newUser = getNewUser();
        newUser.setId(userId);

        USER_MATCHER.assertMatch(savedUser, newUser);
        USER_MATCHER.assertMatch(userDAO.get(userId), newUser);
    }

    @Test
    void update() {
        userDAO.update(getUpdatedUser());
        USER_MATCHER.assertMatch(userDAO.get(USER_1_ID), getUpdatedUser());
    }

    @Test
    void delete() {
        userDAO.delete(USER_1_ID);
        Assertions.assertNull(userDAO.get(USER_1_ID));
    }

    @Test
    void get() {
        User retrievedUser = userDAO.get(USER_1_ID);
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }

    @Test
    void findUsernamesByFirstNameAndLastName() {
        List<String> usernames = userDAO.findUsernamesByFirstNameAndLastName(USER_1.getFirstName(), USER_1.getLastName());
        Assertions.assertEquals(List.of(USER_1.getUsername()), usernames);
    }

    @Test
    void getByUsername() {
        User retrievedUser = userDAO.getByUsername(USER_1.getUsername());
        USER_MATCHER.assertMatch(retrievedUser, USER_1);

    }

    @Test
    void changePassword() {
        userDAO.changePassword(USER_1_ID, "newPassword");
        User retrievedUser = userDAO.get(USER_1_ID);
        User updatedUser = new User(USER_1);
        updatedUser.setPassword("newPassword");
        USER_MATCHER.assertMatch(retrievedUser, updatedUser);
    }

    @Test
    void active() {
        assertTrue(userDAO.active(USER_1_ID, false));
        assertFalse(userDAO.get(USER_1_ID).isActive());
        assertTrue(userDAO.active(USER_1_ID, true));
        clearSession();
        assertTrue(userDAO.get(USER_1_ID).isActive());
    }

    @Test
    void deleteByUsername() {
        userDAO.deleteByUsername(USER_1.getUsername());
        Assertions.assertNull(userDAO.get(USER_1_ID));
    }

    @Test
    void getByUsernameAndPassword() {
        User retrievedUser = userDAO.getByUsernameAndPassword(USER_1.getUsername(), USER_1.getPassword());
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }
}