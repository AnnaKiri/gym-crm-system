package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dto.TrainingInfoDto;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.feign.TrainerWorkloadServiceFeignClient;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.repository.TrainingRepository;
import com.kirillova.gymcrmsystem.repository.TrainingTypeRepository;
import com.kirillova.gymcrmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

    public Training get(int id) {
        log.debug("Get training with trainingId = {}", id);
        return trainingRepository.getTrainingIfExists(id);
    }

    public Training getFull(int id) {
        log.debug("Get full training with trainingId = {}", id);
        return trainingRepository
                .getFullTrainingById(id)
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));

    }

    public Training create(Trainee trainee, Trainer trainer, String name, Integer typeId, LocalDate date, int duration) {
        log.debug("Create new training");
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setName(name);
        training.setType(trainingTypeRepository.getTrainingTypeIfExists(typeId));
        training.setDate(date);
        training.setDuration(duration);
        ValidationUtil.validate(training);
        Training savedTraining = trainingRepository.save(training);
        TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().isActive())
                .date(date)
                .duration(duration)
                .actionType(TrainingInfoDto.ACTION_TYPE_ADD)
                .build();
        trainerWorkloadServiceFeignClient.updateTrainingInfo(trainingInfoDto);
        return savedTraining;
    }
}
