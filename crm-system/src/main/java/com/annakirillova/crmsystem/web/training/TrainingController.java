package com.annakirillova.crmsystem.web.training;

import com.annakirillova.crmsystem.dto.TrainingDto;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.annakirillova.crmsystem.util.TrainingUtil.createDto;
import static com.annakirillova.crmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TrainingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Training Controller", description = "Managing gym trainings")
public class TrainingController {
    static final String REST_URL = "/trainings";

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @GetMapping("/{id}")
    @Operation(summary = "Get training details", description = "Gets the details of the specified training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    public TrainingDto get(@PathVariable int id) {
        log.debug("Get the training with id={}", id);
        Training training = trainingService.getFull(id);
        return createDto(training);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Create a new training", description = "Creates a new training with the specified details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public void create(@Valid @RequestBody TrainingDto trainingDto) {
        log.debug("Create a new training {}", trainingDto);
        checkNew(trainingDto);
        Trainee trainee = traineeService.get(trainingDto.getTraineeUsername());
        Trainer trainer = trainerService.get(trainingDto.getTrainerUsername());
        trainingService.create(
                trainee,
                trainer,
                trainingDto.getName(),
                trainingDto.getTypeId(),
                trainingDto.getDate(),
                trainingDto.getDuration());
    }
}
