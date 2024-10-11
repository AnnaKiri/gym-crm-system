package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.dto.TrainingInfoDto;
import com.annakirillova.trainerworkloadservice.exception.IllegalRequestDataException;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageListenerService {

    public static final String TRAINER_WORKLOAD_QUEUE = "trainer-workload";
    public static final String TRAINER_WORKLOAD_DEAD_LETTER_QUEUE = "ActiveMQ.DLQ";

    private final TrainerService trainerService;
    private final SummaryService summaryService;

    @JmsListener(destination = TRAINER_WORKLOAD_QUEUE, containerFactory = "jmsFactory")
    @Transactional
    public void receiveMessage(@Valid TrainingInfoDto trainingInfoDto) {
        log.debug("{} a new training {}", trainingInfoDto.getActionType(), trainingInfoDto);
        switch (trainingInfoDto.getActionType()) {
            case TrainingInfoDto.ACTION_TYPE_ADD:
                Trainer trainer = trainerService.create(
                        trainingInfoDto.getFirstName(),
                        trainingInfoDto.getLastName(),
                        trainingInfoDto.getUsername(),
                        trainingInfoDto.getIsActive());
                summaryService.addOrUpdateTrainingDuration(
                        trainer.getUsername(),
                        trainingInfoDto.getDate(),
                        trainingInfoDto.getDuration());
                break;
            case TrainingInfoDto.ACTION_TYPE_DELETE:
                summaryService.deleteTrainingDurationFromSummaryByDateAndUsername(
                        trainingInfoDto.getUsername(),
                        trainingInfoDto.getDate(),
                        trainingInfoDto.getDuration());
                break;
            default:
                log.debug("Wrong action type {}", trainingInfoDto.getActionType());
                throw new IllegalRequestDataException("Wrong action type");
        }
    }

    @JmsListener(destination = TRAINER_WORKLOAD_DEAD_LETTER_QUEUE, containerFactory = "jmsFactory")
    public void receiveDeadLetterMessage(TrainingInfoDto trainingInfoDto) {
        log.error("Can't handle a message {}: ", trainingInfoDto);
    }
}

