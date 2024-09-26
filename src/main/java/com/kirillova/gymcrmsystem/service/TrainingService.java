package com.kirillova.gymcrmsystem.service;

import com.kirillova.gymcrmsystem.dto.TrainingInfoDto;
import com.kirillova.gymcrmsystem.error.DataConflictException;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.feign.TrainerWorkloadServiceFeignClient;
import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.repository.TrainingRepository;
import com.kirillova.gymcrmsystem.repository.TrainingTypeRepository;
import com.kirillova.gymcrmsystem.security.JWTProvider;
import com.kirillova.gymcrmsystem.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;
    private final AuthService authService;

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

        String jwtToken = authService.getJwtToken();
        if (jwtToken == null) {
            log.error("JWT token is missing, unable to make a call to trainerWorkloadService.");
            throw new DataConflictException("JWT token is missing");
        }

        TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().isActive())
                .date(date)
                .duration(duration)
                .actionType(TrainingInfoDto.ACTION_TYPE_ADD)
                .build();
        trainerWorkloadServiceFeignClient.updateTrainingInfo(JWTProvider.BEARER_PREFIX + jwtToken,
                MDC.get("transactionId"),
                trainingInfoDto);
        return savedTraining;
    }
}
