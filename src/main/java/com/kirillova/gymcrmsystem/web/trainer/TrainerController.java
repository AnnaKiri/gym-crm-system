package com.kirillova.gymcrmsystem.web.trainer;

import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
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
import static com.kirillova.gymcrmsystem.util.ValidationUtil.assureIdConsistent;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequestMapping(value = TrainerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainerController {
    static final String REST_URL = "/trainer";

    @Autowired
    protected TrainerService trainerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> register(@Valid @RequestBody TrainerTo trainerTo) {
        log.info("Register a new trainee {}", trainerTo);
        checkNew(trainerTo);
        Trainer newTrainer = trainerService.create(trainerTo.getFirstName(), trainerTo.getLastName(), trainerTo.getSpecialization());
        User newUser = newTrainer.getUser();
        UserTo userTo = new UserTo(newUser.getId(), newUser.getUsername(), newUser.getPassword());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(userTo);
    }

    @PutMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody UserTo userTo, @PathVariable int id) {
        log.info("Change password for user {} with id={}", userTo, id);
        Trainer trainer = trainerService.getByUsername(userTo.getUsername());
        assureIdConsistent(trainer, id);
        trainerService.changePassword(id, userTo.getNewPassword());
    }

    @GetMapping("/{id}")
    public TrainerTo get(@PathVariable int id) {
        log.info("Get the trainer with id={}", id);
        Trainer receivedTrainer = trainerService.getWithTrainees(id);
        return createToWithTraineeToList(receivedTrainer);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public TrainerTo update(@PathVariable int id, @Valid @RequestBody TrainerTo trainerTo) {
        log.info("Update the trainer with id {}", id);
//        assureIdConsistent(dish, id); // может не надо?
        // еще I. Username (required)
        trainerService.update(id, trainerTo.getFirstName(), trainerTo.getLastName(), trainerTo.getSpecialization(), trainerTo.isActive());
        Trainer updatedTrainer = trainerService.getWithTrainees(id);
        return createToWithTraineeToList(updatedTrainer);
    }

    // надо проверять что trainer еще и активен. 10 пункт


    @GetMapping("/{id}/trainings")
    public List<TrainingTo> getTrainings(
            @PathVariable int id,
            @Valid @RequestBody TrainerTo trainerTo,
            @RequestParam @Nullable LocalDate fromDate,
            @RequestParam @Nullable LocalDate toDate,
            @RequestParam @Nullable String traineeFirstName,
            @RequestParam @Nullable String traineeLastName) {
        log.debug("Get Trainings by trainer username {} for dates({} - {}) trainee {} {}", trainerTo.getUsername(), fromDate, toDate, traineeFirstName, traineeLastName);
        List<Training> trainings = trainerService.getTrainings(trainerTo.getUsername(), fromDate, toDate, traineeFirstName, traineeLastName);
        return getTrainingToList(trainings, traineeFirstName, traineeLastName);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void setActive(@PathVariable int id, @RequestParam boolean isActive) {
        log.info(isActive ? "enable {}" : "disable {}", id);
//        assureIdConsistent(dish, id); // может не надо?
        trainerService.setActive(id, isActive);
    }
}
