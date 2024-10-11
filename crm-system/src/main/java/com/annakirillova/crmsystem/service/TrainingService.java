package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
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

import static com.annakirillova.crmsystem.service.MessageSenderService.TRAINER_WORKLOAD_QUEUE;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final MessageSenderService messageSenderService;

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
        messageSenderService.sendMessage(TRAINER_WORKLOAD_QUEUE, trainingInfoDto);
        return savedTraining;
    }
}
