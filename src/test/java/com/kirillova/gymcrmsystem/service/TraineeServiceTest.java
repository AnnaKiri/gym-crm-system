package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.config.SpringConfig;
import com.kirillova.gymcrmsystem.dao.TraineeDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.kirillova.gymcrmsystem.TestData.newTrainee;
import static com.kirillova.gymcrmsystem.TestData.newUser;
import static com.kirillova.gymcrmsystem.TestData.trainee1;
import static com.kirillova.gymcrmsystem.TestData.updatedTrainee;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {SpringConfig.class})
public class TraineeServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private Set<String> allUsernames;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    public void setUp() {
        allUsernames = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Test
    public void create() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(9L);
            return user;
        });

        when(traineeDAO.save(any(Trainee.class))).thenAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(5L);
            return trainee;
        });
        Trainee result = traineeService.create(newUser.getFirstName(), newUser.getLastName(), newTrainee.getDateOfBirth(), newTrainee.getAddress());

        verify(userDAO, times(1)).save(any(User.class));
        verify(traineeDAO, times(1)).save(any(Trainee.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(newTrainee, result);
    }

    @Test
    public void get() {
        when(traineeDAO.getTrainee(trainee1.getId())).thenReturn(trainee1);
        Assertions.assertEquals(trainee1, traineeService.get(trainee1.getId()));
    }

    @Test
    public void delete() {
        when(traineeDAO.getTrainee(1L)).thenReturn(trainee1);
        traineeService.delete(1L);
        verify(traineeDAO, times(1)).delete(1L);
        verify(userDAO, times(1)).delete(trainee1.getUserId());
        when(traineeDAO.getTrainee(1L)).thenReturn(null);
        Assertions.assertNull(traineeService.get(1L));
    }

    @Test
    public void update() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setId(2L);
        trainee.setUserId(user.getId());

        when(traineeDAO.getTrainee(1L)).thenReturn(trainee);
        when(userDAO.getUser(2L)).thenReturn(user);

        traineeService.update(1L, "updatedFirstName", "updatedLastName", LocalDate.of(1976, 4, 10), "updated address", false);

        verify(userDAO, times(1)).update(2L, user);
        verify(traineeDAO, times(1)).update(1L, trainee);
        when(traineeDAO.getTrainee(1L)).thenReturn(updatedTrainee);
        Assertions.assertEquals(updatedTrainee, traineeService.get(1L));
    }
}
