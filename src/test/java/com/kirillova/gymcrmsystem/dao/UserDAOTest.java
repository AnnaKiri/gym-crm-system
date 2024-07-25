package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.getNewUser;
import static com.kirillova.gymcrmsystem.UserTestData.getUpdatedUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOTest extends AbstractSpringTest {

    @Autowired
    private UserDAO userDAO;

    @Test
    void save() {
        User savedUser = userDAO.save(getNewUser());
        int userId = savedUser.getId();
        User newUser = getNewUser();
        newUser.setId(userId);

        USER_MATCHER.assertMatch(savedUser, newUser);
        USER_MATCHER.assertMatch(userDAO.get(savedUser.getUsername()), newUser);
    }

    @Test
    void update() {
        User user = getUpdatedUser();
        userDAO.update(user);
        USER_MATCHER.assertMatch(userDAO.get(user.getUsername()), user);
    }

    @Test
    void delete() {
        userDAO.delete(USER_1_USERNAME);
        Assertions.assertNull(userDAO.get(USER_1_USERNAME));
    }

    @Test
    void get() {
        User retrievedUser = userDAO.get(USER_1_USERNAME);
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }

    @Test
    void findUsernamesByFirstNameAndLastName() {
        List<String> usernames = userDAO.findUsernamesByFirstNameAndLastName(USER_1.getFirstName(), USER_1.getLastName());
        Assertions.assertEquals(List.of(USER_1.getUsername()), usernames);
    }

    @Test
    void getByUsername() {
        User retrievedUser = userDAO.get(USER_1.getUsername());
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }

    @Test
    void changePassword() {
        userDAO.changePassword(USER_1_USERNAME, "newPassword");
        User retrievedUser = userDAO.get(USER_1_USERNAME);
        User updatedUser = new User(USER_1);
        updatedUser.setPassword("newPassword");
        USER_MATCHER.assertMatch(retrievedUser, updatedUser);
    }

    @Test
    void active() {
        assertTrue(userDAO.setActive(USER_1_USERNAME, false));
        assertFalse(userDAO.get(USER_1_USERNAME).isActive());
        assertTrue(userDAO.setActive(USER_1_USERNAME, true));
        clearSession();
        assertTrue(userDAO.get(USER_1_USERNAME).isActive());
    }

    @Test
    void deleteByUsername() {
        userDAO.delete(USER_1.getUsername());
        Assertions.assertNull(userDAO.get(USER_1_USERNAME));
    }

    @Test
    void getByUsernameAndPassword() {
        User retrievedUser = userDAO.getByUsernameAndPassword(USER_1.getUsername(), USER_1.getPassword());
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }
}