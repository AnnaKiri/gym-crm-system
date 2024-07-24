package com.kirillova.gymcrmsystem.web.training;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.kirillova.gymcrmsystem.util.TrainingUtil.createTo;
import static com.kirillova.gymcrmsystem.util.ValidationUtil.checkNew;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TrainingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainingController {
    static final String REST_URL = "/training";

    protected TrainingService trainingService;
    protected TraineeService traineeService;
    protected TrainerService trainerService;

    @GetMapping("/{id}")
    public TrainingTo get(@PathVariable int id) {
        log.info("Get the training with id={}", id);
        Training training = trainingService.get(id);
        return createTo(training);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void create(@Valid @RequestBody TrainingTo trainingTo) {
        log.info("Create a new training {}", trainingTo);
        checkNew(trainingTo);
        Trainee trainee = traineeService.get(trainingTo.getTraineeName());
        Trainer trainer = trainerService.get(trainingTo.getTrainerName());
        trainingService.create(trainee, trainer, trainingTo.getName(), trainingTo.getType(), trainingTo.getDate(), trainingTo.getDuration());
    }
}
