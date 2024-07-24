package com.kirillova.gymcrmsystem.facade;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.service.AuthenticationService;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final AuthenticationService authService;

    // Methods for Trainee
    public void createTrainee(String firstName, String lastName, LocalDate birthday, String address) {
        traineeService.create(firstName, lastName, birthday, address);
    }

    public void updateTrainee(String username, String password, int traineeId, String firstName, String lastName, LocalDate birthday, String address, boolean isActive) {
        authService.userAuthentication(username, password);
        traineeService.update(traineeId, firstName, lastName, birthday, address, isActive);
    }

    public Trainee getTrainee(String username, String password, int traineeId) {
        authService.userAuthentication(username, password);
        return traineeService.get(traineeId);
    }

    public void deleteTrainee(String username, String password, int traineeId) {
        authService.userAuthentication(username, password);
        traineeService.delete(traineeId);
    }

    public Trainee getTraineeByUserName(String username, String password) {
        authService.userAuthentication(username, password);
        return traineeService.getByUsername(username);
    }

    public boolean changeTraineePassword(String username, String password, int traineeId, String newPassword) {
        authService.userAuthentication(username, password);
        return traineeService.changePassword(traineeId, newPassword);
    }

    public boolean activeTrainee(String username, String password, int traineeId, boolean isActive) {
        authService.userAuthentication(username, password);
        return traineeService.setActive(traineeId, isActive);
    }

    public void deleteTraineeByUsername(String username, String password) {
        authService.userAuthentication(username, password);
        traineeService.deleteByUsername(username);
    }

    public List<Trainer> getFreeTrainersForTrainee(String username, String password) {
        authService.userAuthentication(username, password);
        return traineeService.getFreeTrainersForTrainee(username);
    }

    public Trainee getTraineeWithTrainers(String username, String password, int traineeId) {
        authService.userAuthentication(username, password);
        return traineeService.getWithTrainers(traineeId);
    }

    private List<Training> getTraineeTrainings(String username, String password, LocalDate fromDate, LocalDate toDate, String trainingType, String trainerFirstName, String trainerLastName) {
        authService.userAuthentication(username, password);
        return traineeService.getTrainings(username, fromDate, toDate, trainingType, trainerFirstName, trainerLastName);
    }

    // Methods for Trainer
    public void createTrainer(String firstName, String lastName, TrainingType specialization) {
        trainerService.create(firstName, lastName, specialization);
    }

    public void updateTrainer(String username, String password, int trainerId, String firstName, String lastName, TrainingType specialization, boolean isActive) {
        authService.userAuthentication(username, password);
        trainerService.update(trainerId, firstName, lastName, specialization, isActive);
    }

    public Trainer getTrainer(String username, String password, int trainerId) {
        authService.userAuthentication(username, password);
        return trainerService.get(trainerId);
    }

    public void getTrainerByUserName(String username, String password) {
        authService.userAuthentication(username, password);
        trainerService.getByUsername(username);
    }

    public boolean changeTrainerPassword(String username, String password, int trainerId, String newPassword) {
        authService.userAuthentication(username, password);
        return trainerService.changePassword(trainerId, newPassword);
    }

    public boolean activeTrainer(String username, String password, int trainerId, boolean isActive) {
        authService.userAuthentication(username, password);
        return trainerService.setActive(trainerId, isActive);
    }

    public List<Training> getTrainerTrainings(String username, String password, LocalDate fromDate, LocalDate toDate, String traineeFirstName, String traineeLastName) {
        authService.userAuthentication(username, password);
        return trainerService.getTrainings(username, fromDate, toDate, traineeFirstName, traineeLastName);
    }

    // Methods for Training
    public void createTraining(String username, String password, Trainee trainee, Trainer trainer, String name, TrainingType type, LocalDate date, int duration) {
        authService.userAuthentication(username, password);
        trainingService.create(trainee, trainer, name, type, date, duration);
    }

    public Training getTraining(String username, String password, int trainingId) {
        authService.userAuthentication(username, password);
        return trainingService.get(trainingId);
    }
}
