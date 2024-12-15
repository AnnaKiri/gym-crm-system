package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.common.dto.TrainingInfoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!dev")
public class MessageListenerActiveMQ {

    private static final String TRAINER_WORKLOAD_QUEUE = "trainer-workload";
    private static final String TRAINER_WORKLOAD_DEAD_LETTER_QUEUE = "trainer-workload-dlq";

    private final TrainerSummaryService trainerSummaryService;

    @JmsListener(destination = TRAINER_WORKLOAD_QUEUE, containerFactory = "jmsFactory")
    public void receiveMessage(@Valid TrainingInfoDto trainingInfoDto) {
        log.debug("{} a new training {}", trainingInfoDto.getActionType(), trainingInfoDto);

        switch (trainingInfoDto.getActionType()) {
            case ADD:
                TrainerSummaryDto trainerSummary = trainerSummaryService.create(
                        trainingInfoDto.getFirstName(),
                        trainingInfoDto.getLastName(),
                        trainingInfoDto.getUsername(),
                        trainingInfoDto.getIsActive());
                trainerSummaryService.addOrUpdateTrainingDuration(
                        trainerSummary.getUsername(),
                        trainingInfoDto.getDate(),
                        trainingInfoDto.getDuration());
                break;
            case DELETE:
                trainerSummaryService.deleteTrainingDurationFromSummaryByDateAndUsername(
                        trainingInfoDto.getUsername(),
                        trainingInfoDto.getDate(),
                        trainingInfoDto.getDuration());
                break;
        }
    }

    @JmsListener(destination = TRAINER_WORKLOAD_DEAD_LETTER_QUEUE, containerFactory = "jmsFactory")
    public void receiveDeadLetterMessage(Object rawMessage) {
        log.error("Received message in DLQ. Raw message: {}", rawMessage);
    }
}