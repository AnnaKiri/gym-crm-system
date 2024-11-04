package com.annakirillova.crmsystem.service;

import com.annakirillova.common.dto.ActionType;
import com.annakirillova.common.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.repository.TrainingRepository;
import com.annakirillova.crmsystem.repository.TrainingTypeRepository;
import com.annakirillova.crmsystem.util.ValidationUtil;
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
    private final TrainerMessageSender trainerMessageSenderService;

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
                .actionType(ActionType.ADD)
                .build();
        trainerMessageSenderService.sendMessage(trainingInfoDto);
        return savedTraining;
    }
}
