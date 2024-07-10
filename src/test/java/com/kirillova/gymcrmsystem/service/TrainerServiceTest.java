package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dao.TrainerDAO;
import com.kirillova.gymcrmsystem.dao.UserDAO;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.kirillova.gymcrmsystem.TestData.newTrainer;
import static com.kirillova.gymcrmsystem.TestData.newUser;
import static com.kirillova.gymcrmsystem.TestData.trainer1;
import static com.kirillova.gymcrmsystem.TestData.trainingType4;
import static com.kirillova.gymcrmsystem.TestData.updatedTrainer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private Set<String> allUsernames;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    public void setUp() {
        allUsernames = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Test
    void get() {
        when(trainerDAO.getTrainer(trainer1.getId())).thenReturn(trainer1);
        Assertions.assertEquals(trainer1, trainerService.get(trainer1.getId()));
    }

    @Test
    void update() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        User user = new User();
        user.setId(5L);
        trainer.setUserId(user.getId());

        when(trainerDAO.getTrainer(1L)).thenReturn(trainer);
        when(userDAO.getUser(5L)).thenReturn(user);

        trainerService.update(1L, "updatedFirstName", "updatedLastName", 2L, false);

        verify(userDAO, times(1)).update(5L, user);
        verify(trainerDAO, times(1)).update(1L, trainer);
        when(trainerDAO.getTrainer(1L)).thenReturn(updatedTrainer);
        Assertions.assertEquals(updatedTrainer, trainerService.get(1L));
    }

    @Test
    void create() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(9L);
            return user;
        });

        when(trainerDAO.save(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(5L);
            return trainer;
        });
        Trainer result = trainerService.create(newUser.getFirstName(), newUser.getLastName(), trainingType4.getId());

        verify(userDAO, times(1)).save(any(User.class));
        verify(trainerDAO, times(1)).save(any(Trainer.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(newTrainer, result);
    }
}