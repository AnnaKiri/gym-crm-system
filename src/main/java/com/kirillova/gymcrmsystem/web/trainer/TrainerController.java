package com.kirillova.gymcrmsystem.web.trainer;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.service.AuthenticationService;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.to.TrainerTo;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import static com.kirillova.gymcrmsystem.util.TrainerUtil.createToWithTraineeToList;
import static com.kirillova.gymcrmsystem.util.TrainingUtil.getTrainingToList;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TrainerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainer Controller", description = "Managing gym trainers")
public class TrainerController {
    static final String REST_URL = "/trainers";

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new trainer", description = "Creates a new trainer and associated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<UserTo> register(@Valid @RequestBody TrainerTo trainerTo) {
        log.debug("Register a new trainer {}", trainerTo);
        checkNew(trainerTo);
        Trainer newTrainer = trainerService.create(trainerTo.getFirstName(), trainerTo.getLastName(), trainerTo.getSpecializationId());
        User newUser = newTrainer.getUser();
        UserTo userTo = new UserTo(newUser.getId(), newUser.getUsername(), newUser.getPassword());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{username}").buildAndExpand(userTo.getUsername()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(userTo);
    }

    @PutMapping(value = "/{username}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change password", description = "Changes the password of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public void changePassword(@Valid @RequestBody UserTo userTo, @PathVariable String username) {
        log.debug("Change password for user {} with username={}", userTo, username);
        authenticationService.checkAuthenticatedUser(username, userTo.getPassword());
        trainerService.changePassword(username, userTo.getNewPassword());
    }

    @GetMapping("/{username}")
    @Transactional
    @Operation(summary = "Get trainer details", description = "Gets the details of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public TrainerTo get(@PathVariable String username) {
        log.debug("Get the trainer with username={}", username);
        Trainer receivedTrainer = trainerService.getWithUserAndSpecialization(username);
        List<Trainee> traineeList = traineeService.getTraineesForTrainer(username);
        return createToWithTraineeToList(receivedTrainer, traineeList);
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
    public TrainerTo update(@PathVariable String username, @Valid @RequestBody TrainerTo trainerTo) {
        log.debug("Update the trainer with username {}", username);
        trainerService.update(username, trainerTo.getFirstName(), trainerTo.getLastName(), trainerTo.getSpecializationId(), trainerTo.getIsActive());
        Trainer receivedTrainer = trainerService.getWithUserAndSpecialization(username);
        List<Trainee> traineeList = traineeService.getTraineesForTrainer(username);
        return createToWithTraineeToList(receivedTrainer, traineeList);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer's trainings", description = "Gets the list of trainings for the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public List<TrainingTo> getTrainings(
            @PathVariable String username,
            @RequestParam @Nullable LocalDate fromDate,
            @RequestParam @Nullable LocalDate toDate,
            @RequestParam @Nullable String traineeFirstName,
            @RequestParam @Nullable String traineeLastName) {
        log.debug("Get Trainings by trainer username {} for dates({} - {}) trainee {} {}", username, fromDate, toDate, traineeFirstName, traineeLastName);
        List<Training> trainings = trainerService.getTrainings(username, fromDate, toDate, traineeFirstName, traineeLastName);
        return getTrainingToList(trainings);
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
