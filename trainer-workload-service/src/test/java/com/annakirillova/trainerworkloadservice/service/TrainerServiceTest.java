package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_ID;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1_USERNAME;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_MATCHER;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.getNewTrainer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void get() {
        when(trainerRepository.getTrainerIfExists(TRAINER_1_USERNAME)).thenReturn(TRAINER_1);

        Trainer foundTrainer = trainerService.get(TRAINER_1_USERNAME);
        TRAINER_MATCHER.assertMatch(foundTrainer, TRAINER_1);
    }

    @Test
    void create() {
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(TRAINER_1_ID + 4);
            return trainer;
        });

        when(trainerRepository.findByUsername("Jim.Carrey")).thenReturn(Optional.empty());

        Trainer newTrainer = getNewTrainer();
        Trainer savedTrainer = trainerService.create("Jim", "Carrey", "Jim.Carrey", true);

        verify(trainerRepository, times(1)).save(any(Trainer.class));

        int trainerId = savedTrainer.getId();
        newTrainer.setId(trainerId);

        TRAINER_MATCHER.assertMatch(savedTrainer, newTrainer);
    }

    @Test
    void createExistedTrainer() {
        when(trainerRepository.findByUsername(TRAINER_1_USERNAME)).thenReturn(Optional.of(TRAINER_1));

        Trainer savedTrainer = trainerService.create(TRAINER_1.getFirstName(), TRAINER_1.getLastName(), TRAINER_1.getUsername(), true);

        verify(trainerRepository, times(0)).save(any(Trainer.class));

        TRAINER_MATCHER.assertMatch(savedTrainer, TRAINER_1);
    }
}