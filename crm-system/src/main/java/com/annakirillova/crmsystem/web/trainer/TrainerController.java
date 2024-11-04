package com.annakirillova.crmsystem.web.trainer;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TrainerDto;
import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.exception.AuthenticationException;
import com.annakirillova.crmsystem.metrics.RegisterMetrics;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.service.AuthService;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.util.UserUtil;
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
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.annakirillova.crmsystem.util.TrainerUtil.createDtoWithTraineeToList;
import static com.annakirillova.crmsystem.util.TrainingUtil.getTrainingDtoList;
import static com.annakirillova.crmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TrainerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainer Controller", description = "Managing gym trainers")
@SecurityRequirement(name = "Bearer Authentication")
public class TrainerController {
    public static final String REST_URL = "/trainers";

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final RegisterMetrics registerMetrics;
    private final AuthService authService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new trainer", description = "Creates a new trainer and associated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @SecurityRequirement(name = "")
    public ResponseEntity<LoginRequestDto> register(@Valid @RequestBody TrainerDto trainerDto) {
        long start = System.nanoTime();

        log.debug("Register a new trainer {}", trainerDto);
        registerMetrics.incrementRequestCount();

        checkNew(trainerDto);
        String password = UserUtil.generatePassword();
        Trainer newTrainer = trainerService.create(
                trainerDto.getFirstName(),
                trainerDto.getLastName(),
                trainerDto.getSpecializationId(),
                password);
        User newUser = newTrainer.getUser();
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                newUser.getUsername(),
                password);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/{username}")
                .buildAndExpand(loginRequestDto.getUsername()).toUri();

        registerMetrics.recordExecutionTimeTrainer(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        return ResponseEntity.created(uriOfNewResource).body(loginRequestDto);
    }

    @PutMapping(value = "/{username}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change password", description = "Changes the password of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public void changePassword(@Valid @RequestBody UserDto userDto, @PathVariable String username) {
        log.debug("Change password for user {} with username={}", userDto, username);
        if (!username.equalsIgnoreCase(authService.getUsername())) {
            throw new AuthenticationException("You can't change password for user " + username + " because you're " + authService.getUsername());
        }
        trainerService.changePassword(username, userDto.getNewPassword());
    }

    @GetMapping("/{username}")
    @Transactional
    @Operation(summary = "Get trainer details", description = "Gets the details of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public TrainerDto get(@PathVariable String username) {
        log.debug("Get the trainer with username={}", username);
        Trainer receivedTrainer = trainerService.getWithUserAndSpecialization(username);
        List<Trainee> traineeList = traineeService.getTraineesForTrainer(username);
        return createDtoWithTraineeToList(receivedTrainer, traineeList);
    }

    @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Update trainer details", description = "Updates the details of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public TrainerDto update(@PathVariable String username, @Valid @RequestBody TrainerDto trainerDto) {
        log.debug("Update the trainer with username {}", username);
        trainerService.update(
                username,
                trainerDto.getFirstName(),
                trainerDto.getLastName(),
                trainerDto.getSpecializationId(),
                trainerDto.getIsActive());
        Trainer receivedTrainer = trainerService.getWithUserAndSpecialization(username);
        List<Trainee> traineeList = traineeService.getTraineesForTrainer(username);
        return createDtoWithTraineeToList(receivedTrainer, traineeList);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer's trainings", description = "Gets the list of trainings for the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public List<TrainingDto> getTrainings(
            @PathVariable String username,
            @RequestParam @Nullable LocalDate fromDate,
            @RequestParam @Nullable LocalDate toDate,
            @RequestParam @Nullable String traineeFirstName,
            @RequestParam @Nullable String traineeLastName) {
        log.debug("Get Trainings by trainer username {} for dates({} - {}) trainee {} {}", username, fromDate, toDate, traineeFirstName, traineeLastName);
        List<Training> trainings = trainerService.getTrainings(
                username,
                fromDate,
                toDate,
                traineeFirstName,
                traineeLastName);
        return getTrainingDtoList(trainings);
    }

    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set trainer active status", description = "Sets the active status of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "400", description = "Trainer is already in the desired state")
    })
    public void setActive(@PathVariable String username, @RequestParam boolean isActive) {
        log.debug(isActive ? "enable {}" : "disable {}", username);
        trainerService.setActive(username, isActive);
    }
}
