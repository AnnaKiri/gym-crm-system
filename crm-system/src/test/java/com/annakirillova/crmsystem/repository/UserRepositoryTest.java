package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.BaseTest;
import com.annakirillova.crmsystem.error.NotFoundException;
import com.annakirillova.crmsystem.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.annakirillova.crmsystem.UserTestData.NOT_FOUND_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_MATCHER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findIsActiveByUsername() {
        assertTrue(userRepository.findIsActiveByUsername(USER_1_USERNAME));
    }

    @Test
    void deleteByUsername() {
        int changedLines = userRepository.deleteByUsername(USER_1_USERNAME);
        Assertions.assertEquals(1, changedLines);
        Assertions.assertTrue(userRepository.findByUsername(USER_1_USERNAME).isEmpty());
    }

    @Test
    void updateIsActiveByUsername() {
        int changedLines = userRepository.updateIsActiveByUsername(USER_1_USERNAME, false);
        Assertions.assertEquals(1, changedLines);
        assertFalse(userRepository.findByUsername(USER_1_USERNAME).get().isActive());
        changedLines = userRepository.updateIsActiveByUsername(USER_1_USERNAME, true);
        entityManager.clear();
        Assertions.assertEquals(changedLines, 1);
        assertTrue(userRepository.findByUsername(USER_1_USERNAME).get().isActive());
    }

    @Test
    void findUsernamesByFirstNameAndLastName() {
        List<String> usernames = userRepository.findUsernamesByFirstNameAndLastName(USER_1.getFirstName(), USER_1.getLastName());
        Assertions.assertEquals(List.of(USER_1.getUsername()), usernames);
    }

    @Test
    void findByUsername() {
        User retrievedUser = userRepository.findByUsername(USER_1_USERNAME).orElse(null);
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }

    @Test
    void getUserIfExistsWithWrongUsername() {
        User retrievedUser = userRepository.getUserIfExists(USER_1_USERNAME);
        USER_MATCHER.assertMatch(retrievedUser, USER_1);
    }

    @Test
    void getUserIfExistsNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> userRepository.getUserIfExists(NOT_FOUND_USERNAME));
    }
}
