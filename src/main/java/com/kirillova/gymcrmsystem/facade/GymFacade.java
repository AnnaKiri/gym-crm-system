package com.kirillova.gymcrmsystem.facade;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    // Methods for Trainee
    public void createTrainee(String firstName, String lastName, LocalDate birthday, String address) {
        traineeService.create(firstName, lastName, birthday, address);
    }

    public void updateTrainee(long traineeId, String firstName, String lastName, LocalDate birthday, String address, boolean isActive) {
        traineeService.update(traineeId, firstName, lastName, birthday, address, isActive);
    }

    public Trainee getTrainee(long traineeId) {
        return traineeService.get(traineeId);
    }

    public void deleteTrainee(long traineeId) {
        traineeService.delete(traineeId);
    }

    // Methods for Trainer
    public void createTrainer(String firstName, String lastName, long specializationId) {
        trainerService.create(firstName, lastName, specializationId);
    }

    public void updateTrainer(long trainerId, String firstName, String lastName, long specializationId, boolean isActive) {
        trainerService.update(trainerId, firstName, lastName, specializationId, isActive);
    }

    public Trainer getTrainer(long trainerId) {
        return trainerService.get(trainerId);
    }

    // Methods for Training
    public void createTraining(long traineeId, long trainerId, String name, long typeId, LocalDate date, int duration) {
        trainingService.create(traineeId, trainerId, name, typeId, date, duration);
    }

    public Training getTraining(long trainingId) {
        return trainingService.get(trainingId);
    }
}
