package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.service.AuthenticationService;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import com.kirillova.gymcrmsystem.to.TrainerTo;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

import static com.kirillova.gymcrmsystem.util.TraineeUtil.createToWithTrainerToList;
import static com.kirillova.gymcrmsystem.util.TrainerUtil.getTrainerToList;
import static com.kirillova.gymcrmsystem.util.TrainingUtil.getTrainingToList;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequestMapping(value = TraineeController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TraineeController {
    static final String REST_URL = "/trainee";

    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> register(@Valid @RequestBody TraineeTo traineeTo) {
        log.info("Register a new trainee {}", traineeTo);
        checkNew(traineeTo);
        Trainee newTrainee = traineeService.create(traineeTo.getFirstName(), traineeTo.getLastName(), traineeTo.getBirthday(), traineeTo.getAddress());
        User newUser = newTrainee.getUser();
        UserTo userTo = new UserTo(newUser.getId(), newUser.getUsername(), newUser.getPassword());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{username}").buildAndExpand(userTo.getUsername()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(userTo);
    }

    @PutMapping(value = "/{username}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void changePassword(@Valid @RequestBody UserTo userTo, @PathVariable String username) {   // @PathVariable можно не считывать, userTo несет в себе username?
        log.info("Change password for user {} with username={}", userTo, username);
        authenticationService.getAuthenticatedUser(userTo.getUsername(), userTo.getPassword());
        traineeService.changePassword(username, userTo.getNewPassword());
    }

    @GetMapping("/{username}")
    @Transactional
    public TraineeTo get(@PathVariable String username) {
        log.info("Get the trainee with username={}", username);
        Trainee receivedTrainee = traineeService.getWithUser(username);
        List<Trainer> listTrainers = trainerService.getTrainersForTrainee(username);
        return createToWithTrainerToList(receivedTrainee, listTrainers);
    }

    @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public TraineeTo update(@PathVariable String username, @Valid @RequestBody TraineeTo traineeTo) {
        log.info("Update the trainee with username={}", username);
        traineeService.update(username, traineeTo.getFirstName(), traineeTo.getLastName(), traineeTo.getBirthday(), traineeTo.getAddress(), traineeTo.getIsActive());
        Trainee receivedTrainee = traineeService.getWithUser(username);
        List<Trainer> listTrainers = trainerService.getTrainersForTrainee(username);
        return createToWithTrainerToList(receivedTrainee, listTrainers);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String username) {
        log.debug("Delete trainee with username = {}", username);
        traineeService.delete(username);
    }

    @GetMapping("/{username}/free_trainers")
    public List<TrainerTo> getFreeTrainersForTrainee(@PathVariable String username) {
        log.info("Get trainers list that not assigned on trainee with username={}", username);
        List<Trainer> trainers = traineeService.getFreeTrainersForTrainee(username);
        return getTrainerToList(trainers);
    }

    @GetMapping("/{username}/trainings")
    public List<TrainingTo> getTrainings(
            @PathVariable String username,
            @RequestParam @Nullable LocalDate fromDate,
            @RequestParam @Nullable LocalDate toDate,
            @RequestParam @Nullable String trainingType,
            @RequestParam @Nullable String trainerFirstName,
            @RequestParam @Nullable String trainerLastName) {
        log.debug("Get Trainings by trainee username {} for dates({} - {}) trainingType{} trainer {} {}", username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        List<Training> trainings = traineeService.getTrainings(username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        return getTrainingToList(trainings);
    }

    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void setActive(@PathVariable String username, @RequestParam boolean isActive) {
        log.info(isActive ? "enable {}" : "disable {}", username);
        traineeService.setActive(username, isActive);
    }
}
