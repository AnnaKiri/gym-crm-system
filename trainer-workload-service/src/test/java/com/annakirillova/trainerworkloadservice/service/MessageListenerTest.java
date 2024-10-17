package com.annakirillova.trainerworkloadservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_DTO_ADD;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_DTO_DELETE;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_DTO_INVALID_ACTION_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageListenerTest {
    @Mock
    private TrainerService trainerService;

    @Mock
    private SummaryService summaryService;

    @InjectMocks
    private MessageListener messageListener;

    @Test
    void shouldAddTrainingWhenActionIsAdd() {
        when(trainerService.create(anyString(), anyString(), anyString(), anyBoolean())).thenReturn(TRAINER_1);

        messageListener.receiveMessage(TRAINING_DTO_ADD);

        verify(trainerService, times(1)).create(
                TRAINING_DTO_ADD.getFirstName(),
                TRAINING_DTO_ADD.getLastName(),
                TRAINING_DTO_ADD.getUsername(),
                TRAINING_DTO_ADD.getIsActive()
        );
        verify(summaryService, times(1)).addOrUpdateTrainingDuration(
                TRAINING_DTO_ADD.getUsername(),
                TRAINING_DTO_ADD.getDate(),
                TRAINING_DTO_ADD.getDuration()
        );
    }

    @Test
    void shouldDeleteTrainingWhenActionIsDelete() {
        messageListener.receiveMessage(TRAINING_DTO_DELETE);

        verify(summaryService, times(1)).deleteTrainingDurationFromSummaryByDateAndUsername(
                TRAINING_DTO_DELETE.getUsername(),
                TRAINING_DTO_DELETE.getDate(),
                TRAINING_DTO_DELETE.getDuration()
        );
        verify(trainerService, never()).create(anyString(), anyString(), anyString(), anyBoolean());
    }
}
