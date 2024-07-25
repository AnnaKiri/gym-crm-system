package com.kirillova.gymcrmsystem.web.trainer;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.to.TrainerTo;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.kirillova.gymcrmsystem.util.TrainerUtil.createToWithTraineeToList;
import static com.kirillova.gymcrmsystem.util.TrainingUtil.getTrainingToList;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequestMapping(value = TrainerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainerController {
    static final String REST_URL = "/trainer";

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TraineeService traineeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> register(@Valid @RequestBody TrainerTo trainerTo) {
        log.info("Register a new trainee {}", trainerTo);
        checkNew(trainerTo);
        Trainer newTrainer = trainerService.create(trainerTo.getFirstName(), trainerTo.getLastName(), trainerTo.getSpecialization());
        User newUser = newTrainer.getUser();
        UserTo userTo = new UserTo(newUser.getId(), newUser.getUsername(), newUser.getPassword());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{username}").buildAndExpand(userTo.getUsername()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(userTo);
    }

    @PutMapping(value = "/{username}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody UserTo userTo, @PathVariable String username) {   // @PathVariable можно не считывать, userTo несет в себе username?
        log.info("Change password for user {} with username={}", userTo, username);
        trainerService.changePassword(username, userTo.getNewPassword());
    }

    @GetMapping("/{username}")
    @Transactional
    public TrainerTo get(@PathVariable String username) {
        log.info("Get the trainer with username={}", username);
        Trainer receivedTrainer = trainerService.getWithUserAndSpecialization(username);
        List<Trainee> traineeList = traineeService.getTraineesForTrainer(username);
        return createToWithTraineeToList(receivedTrainer, traineeList);
    }

    @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public TrainerTo update(@PathVariable String username, @Valid @RequestBody TrainerTo trainerTo) {
        log.info("Update the trainer with username {}", username);
        trainerService.update(username, trainerTo.getFirstName(), trainerTo.getLastName(), trainerTo.getSpecialization(), trainerTo.getIsActive());
        Trainer receivedTrainer = trainerService.get(username);
        List<Trainee> traineeList = traineeService.getTraineesForTrainer(username);
        return createToWithTraineeToList(receivedTrainer, traineeList);
    }

    @GetMapping("/{username}/trainings")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setActive(@PathVariable String username, @RequestParam boolean isActive) {
        log.info(isActive ? "enable {}" : "disable {}", username);
        trainerService.setActive(username, isActive);
    }
}
