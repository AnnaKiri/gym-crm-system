package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.to.TraineeTo;
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

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.kirillova.gymcrmsystem.util.TraineeUtil.createToWithTrainerToList;
import static com.kirillova.gymcrmsystem.util.TrainerUtil.getTrainerToList;
import static com.kirillova.gymcrmsystem.util.TrainingUtil.getTrainingToList;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.assureIdConsistent;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequestMapping(value = TraineeController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TraineeController {
    static final String REST_URL = "/trainee";

    @Autowired
    protected TraineeService traineeService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTo> register(@Valid @RequestBody TraineeTo traineeTo) {
        log.info("Register a new trainee {}", traineeTo);
        checkNew(traineeTo);
        Trainee newTrainee = traineeService.create(traineeTo.getFirstName(), traineeTo.getLastName(), traineeTo.getBirthday(), traineeTo.getAddress());
        User newUser = newTrainee.getUser();
        UserTo userTo = new UserTo(newUser.getId(), newUser.getUsername(), newUser.getPassword());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(userTo);
    }

    @PutMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody UserTo userTo, @PathVariable int id) {
        log.info("Change password for user {} with id={}", userTo, id);
        Trainee trainee = traineeService.getByUsername(userTo.getUsername());
        assureIdConsistent(trainee, id);   // может не надо?
        traineeService.changePassword(id, userTo.getNewPassword());
    }

    @GetMapping("/{id}")
    public TraineeTo get(@PathVariable int id) {
        log.info("Get the trainee with id={}", id);
        Trainee receivedTrainee = traineeService.getWithTrainers(id);
        return createToWithTrainerToList(receivedTrainee);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public TraineeTo update(@PathVariable int id, @Valid @RequestBody TraineeTo traineeTo) {
        log.info("Update the trainee with id {}", id);
//        assureIdConsistent(dish, id); // может не надо?
        // еще I. Username (required)
        traineeService.update(id, traineeTo.getFirstName(), traineeTo.getLastName(), traineeTo.getBirthday(), traineeTo.getAddress(), traineeTo.isActive());
        Trainee updatedTrainee = traineeService.getWithTrainers(id);
        return createToWithTrainerToList(updatedTrainee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id, @Valid @RequestBody TraineeTo traineeTo) {
        traineeService.deleteByUsername(traineeTo.getUsername());
    }

    @GetMapping("/{id}")
    public List<TrainerTo> getFreeTrainersForTrainee(@PathVariable int id, @Valid @RequestBody TraineeTo traineeTo) {
        log.info("Get trainers list that not assigned on trainee with traineeId={}", id);
        List<Trainer> trainers = traineeService.getFreeTrainersForTrainee(traineeTo.getUsername());
        return getTrainerToList(trainers);
    }

    @GetMapping("/{id}/trainings")
    public List<TrainingTo> getTrainings(
            @PathVariable int id,
            @Valid @RequestBody TraineeTo traineeTo,
            @RequestParam @Nullable LocalDate fromDate,
            @RequestParam @Nullable LocalDate toDate,
            @RequestParam @Nullable String trainingType,
            @RequestParam @Nullable String trainerFirstName,
            @RequestParam @Nullable String trainerLastName) {
        log.debug("Get Trainings by trainee username {} for dates({} - {}) trainingType{} trainer {} {}", traineeTo.getUsername(), fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        List<Training> trainings = traineeService.getTrainings(traineeTo.getUsername(), fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
        return getTrainingToList(trainings, trainerFirstName, trainerLastName);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void setActive(@PathVariable int id, @RequestParam boolean isActive) {
        log.info(isActive ? "enable {}" : "disable {}", id);
//        assureIdConsistent(dish, id); // может не надо?
        traineeService.setActive(id, isActive);
    }
}
