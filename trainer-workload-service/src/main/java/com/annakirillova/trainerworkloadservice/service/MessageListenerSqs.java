package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.common.dto.TrainingInfoDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummary;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("dev")
public class MessageListenerSqs {

    private final TrainerSummaryService trainerSummaryService;

    @SqsListener("${aws.sqs.queues.trainer-workload}")
    public void receiveMessage(@Valid TrainingInfoDto trainingInfoDto) {
        log.debug("{} a new training {}", trainingInfoDto.getActionType(), trainingInfoDto);

        switch (trainingInfoDto.getActionType()) {
            case ADD:
                TrainerSummary trainerSummary = trainerSummaryService.create(
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
}