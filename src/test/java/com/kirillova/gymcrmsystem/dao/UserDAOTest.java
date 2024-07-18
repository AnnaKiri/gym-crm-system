package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;

import java.util.Map;

class UserDAOTest {

    private Map<Long, User> userStorage;
    private UserDAO userDAO;
    User testUser;
    private User savedUser;
    private long userId;

//    @BeforeEach
//    public void setUp() {
//        userStorage = new HashMap<>();
//        userDAO = new UserDAO(userStorage);
//        testUser = new User(user1);
//        savedUser = userDAO.save(testUser);
//        userId = savedUser.getId();
//    }
//
//    @Test
//    void save() {
//        assertNotNull(savedUser);
//        assertEquals(savedUser, user1);
//        assertTrue(userStorage.containsKey(savedUser.getId()));
//    }
//
//    @Test
//    void update() {
//        userDAO.update(userId, updatedUserForTrainee);
//        assertEquals(updatedUserForTrainee, userStorage.get(userId));
//    }
//
//    @Test
//    void delete() {
//        userDAO.delete(userId);
//        assertFalse(userStorage.containsKey(userId));
//    }
//
//    @Test
//    void getUser() {
//        User retrievedUser = userDAO.getUser(userId);
//        assertEquals(savedUser, retrievedUser);
//    }
}