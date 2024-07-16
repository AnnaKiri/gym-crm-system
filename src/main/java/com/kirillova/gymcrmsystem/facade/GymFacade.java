package com.kirillova.gymcrmsystem.facade;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    // Methods for Trainee
    public void createTrainee(String firstName, String lastName, Date birthday, String address) {
        traineeService.create(firstName, lastName, birthday, address);
    }

    public void updateTrainee(int traineeId, String firstName, String lastName, Date birthday, String address, boolean isActive) {
        traineeService.update(traineeId, firstName, lastName, birthday, address, isActive);
    }

    public Trainee getTrainee(int traineeId) {
        return traineeService.get(traineeId);
    }

    public void deleteTrainee(int traineeId) {
        traineeService.delete(traineeId);
    }

    // Methods for Trainer
    public void createTrainer(String firstName, String lastName, TrainingType specialization) {
        trainerService.create(firstName, lastName, specialization);
    }

    public void updateTrainer(int trainerId, String firstName, String lastName, TrainingType specialization, boolean isActive) {
        trainerService.update(trainerId, firstName, lastName, specialization, isActive);
    }

    public Trainer getTrainer(int trainerId) {
        return trainerService.get(trainerId);
    }

    // Methods for Training
    public void createTraining(Trainee trainee, Trainer trainer, String name, TrainingType type, Date date, int duration) {
        trainingService.create(trainee, trainer, name, type, date, duration);
    }

    public Training getTraining(int trainingId) {
        return trainingService.get(trainingId);
    }
}
