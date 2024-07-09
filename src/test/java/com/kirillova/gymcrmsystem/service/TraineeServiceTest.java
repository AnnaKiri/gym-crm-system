package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.AppConfig;
import com.kirillova.gymcrmsystem.config.SpringConfig;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, AppConfig.class})
public class TraineeServiceTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private UserDAO userDAO;

    @Test
    public void testCreate() {
        String firstName = "Matthew";
        String lastName = "Mcconaughey";
        LocalDate birthday = LocalDate.of(1969, 11, 4);
        String address = "some address";

        Trainee createdTrainee = traineeService.create(firstName, lastName, birthday, address);

        assertNotNull(createdTrainee);
        assertNotEquals(0, createdTrainee.getId());
        User createdUser = userDAO.getUser(createdTrainee.getUserId());
        assertNotNull(createdUser);
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(birthday, createdTrainee.getDateOfBirth());
        assertEquals(address, createdTrainee.getAddress());
    }
}
