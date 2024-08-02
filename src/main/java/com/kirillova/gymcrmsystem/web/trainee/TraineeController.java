package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.dto.TraineeDto;
import com.kirillova.gymcrmsystem.dto.TrainerDto;
import com.kirillova.gymcrmsystem.dto.TrainingDto;
import com.kirillova.gymcrmsystem.dto.UserDto;
import com.kirillova.gymcrmsystem.error.AuthenticationException;
import com.kirillova.gymcrmsystem.metrics.RegisterMetrics;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.util.UserUtil;
import com.kirillova.gymcrmsystem.web.AuthUser;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import static com.kirillova.gymcrmsystem.util.TraineeUtil.createDtoWithTrainerDtoList;
import static com.kirillova.gymcrmsystem.util.TrainerUtil.getTrainerDtoList;
import static com.kirillova.gymcrmsystem.util.TrainingUtil.getTrainingDtoList;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TraineeController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainee Controller", description = "Managing gym trainees")
@SecurityRequirement(name = "Bearer Authentication")
public class TraineeController {
    static final String REST_URL = "/trainees";

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final RegisterMetrics registerMetrics;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new trainee", description = "Creates a new trainee and associated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @SecurityRequirement(name = "")
    public ResponseEntity<UserDto> register(@Valid @RequestBody TraineeDto traineeDto) {
        long start = System.nanoTime();
        log.debug("Register a new trainee {}", traineeDto);
        registerMetrics.incrementRequestCount();

        checkNew(traineeDto);
        String password = UserUtil.generatePassword();
        Trainee newTrainee = traineeService.create(
                traineeDto.getFirstName(),
                traineeDto.getLastName(),
                traineeDto.getBirthday(),
                traineeDto.getAddress(),
                password);
        User newUser = newTrainee.getUser();
        UserDto userTo = new UserDto(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getPassword());
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/{username}")
                .buildAndExpand(userTo.getUsername()).toUri();

        registerMetrics.recordExecutionTimeTrainee(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        return ResponseEntity.created(uriOfNewResource).body(userTo);
    }

    @PutMapping(value = "/{username}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Change password", description = "Changes the password of the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public void changePassword(@Valid @RequestBody UserDto userDto, @PathVariable String username, @AuthenticationPrincipal AuthUser authUser) {
        log.debug("Change password for user {} with username={}", userDto, username);
        if (!username.equals(authUser.getUser().getUsername())) {
            throw new AuthenticationException("You can't change password for user " + username);
        }
        traineeService.changePassword(username, userDto.getNewPassword());
    }

    @GetMapping("/{username}")
    @Transactional
    @Operation(summary = "Get trainee details", description = "Gets the details of the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public TraineeDto get(@PathVariable String username) {
        log.debug("Get the trainee with username={}", username);
        Trainee receivedTrainee = traineeService.getWithUser(username);
        List<Trainer> listTrainers = trainerService.getTrainersForTrainee(username);
        return createDtoWithTrainerDtoList(receivedTrainee, listTrainers);
    }

    @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Update trainee details", description = "Updates the details of the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public TraineeDto update(@PathVariable String username, @Valid @RequestBody TraineeDto traineeDto) {
        log.debug("Update the trainee with username={}", username);
        traineeService.update(
                username,
                traineeDto.getFirstName(),
                traineeDto.getLastName(),
                traineeDto.getBirthday(),
                traineeDto.getAddress(),
                traineeDto.getIsActive());
        Trainee receivedTrainee = traineeService.getWithUser(username);
        List<Trainer> listTrainers = trainerService.getTrainersForTrainee(username);
        return createDtoWithTrainerDtoList(receivedTrainee, listTrainers);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete trainee", description = "Deletes the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public void delete(@PathVariable String username) {
        log.debug("Delete trainee with username = {}", username);
        traineeService.delete(username);
    }

    @GetMapping("/{username}/free-trainers")
    @Operation(summary = "Get free trainers", description = "Gets the list of trainers that are not assigned to the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Free trainers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public List<TrainerDto> getFreeTrainersForTrainee(@PathVariable String username) {
        log.debug("Get trainers list that not assigned on trainee with username={}", username);
        List<Trainer> trainers = traineeService.getFreeTrainersForTrainee(username);
        return getTrainerDtoList(trainers);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee's trainings", description = "Gets the list of trainings for the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public List<TrainingDto> getTrainings(
            @PathVariable String username,
            @RequestParam @Nullable LocalDate fromDate,
            @RequestParam @Nullable LocalDate toDate,
            @RequestParam @Nullable String trainingType,
            @RequestParam @Nullable String trainerFirstName,
            @RequestParam @Nullable String trainerLastName) {
        log.debug("Get Trainings by trainee username {} for dates({} - {}) trainingType{} trainer {} {}", username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        List<Training> trainings = traineeService.getTrainings(
                username,
                fromDate,
                toDate,
                trainingType,
                trainerFirstName,
                trainerLastName);
        return getTrainingDtoList(trainings);
    }

    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set trainee active status", description = "Sets the active status of the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "400", description = "Trainee is already in the desired state")
    })
    public void setActive(@PathVariable String username, @RequestParam boolean isActive) {
        log.debug(isActive ? "enable {}" : "disable {}", username);
        traineeService.setActive(username, isActive);
    }

    @PutMapping(value = "/{username}/trainers", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Update trainers list", description = "Updates the trainers list of the specified trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public List<TrainerDto> updateTrainerList(@PathVariable String username, @RequestBody List<String> trainerUsernames) {
        log.debug("Update trainers list for trainee with username = {}", username);
        traineeService.updateTrainerList(username, trainerUsernames);
        List<Trainer> listTrainers = trainerService.getTrainersForTrainee(username);
        return getTrainerDtoList(listTrainers);
    }
}
