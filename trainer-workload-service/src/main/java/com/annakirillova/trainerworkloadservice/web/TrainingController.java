package com.annakirillova.trainerworkloadservice.web;

import com.annakirillova.trainerworkloadservice.dto.TrainingDto;
import com.annakirillova.trainerworkloadservice.error.IllegalRequestDataException;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.service.TrainerService;
import com.annakirillova.trainerworkloadservice.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = TrainingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class TrainingController {

    static final String REST_URL = "/trainings";

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void updateTrainingInfo(@Valid @RequestBody TrainingDto trainingDto) {
        log.debug("{} a new training {}", trainingDto.getActionType(), trainingDto);
        switch (trainingDto.getActionType()) {
            case TrainingDto.ACTION_TYPE_ADD:
                Trainer trainer = trainerService.create(
                        trainingDto.getFirstName(),
                        trainingDto.getLastName(),
                        trainingDto.getUsername(),
                        trainingDto.isActive());
                trainingService.create(
                        trainer,
                        trainingDto.getDate(),
                        trainingDto.getDuration());
                break;
            case TrainingDto.ACTION_TYPE_DELETE:
                trainingService.deleteByUsernameAndDateAndDuration(trainingDto.getUsername(), trainingDto.getDate(), trainingDto.getDuration());
                break;
            default:
                log.debug("Wrong action type {}", trainingDto.getActionType());
                throw new IllegalRequestDataException("Wrong action type");
        }
    }
}
