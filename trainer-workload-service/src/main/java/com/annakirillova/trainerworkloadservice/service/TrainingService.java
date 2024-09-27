package com.annakirillova.trainerworkloadservice.service;


import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.model.Training;
import com.annakirillova.trainerworkloadservice.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public Training create(Trainer trainer, LocalDate date, int duration) {
        log.debug("Create new training");
        Training training = new Training();
        training.setTrainer(trainer);
        training.setDate(date);
        training.setDuration(duration);
        return trainingRepository.save(training);
    }

    public Map<Integer, Map<String, Integer>> getMonthlyTrainingSummary(String username) {
        log.debug("Get monthly summary for trainer with username = {}", username);
        List<Training> resultList = trainingRepository.getTrainingsByUsername(username);

        Map<Integer, Map<String, Integer>> monthlySummary = new HashMap<>();

        for (Training training : resultList) {
            int year = training.getDate().getYear();
            Month month = training.getDate().getMonth();
            String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            Map<String, Integer> yearSummary = monthlySummary.computeIfAbsent(year, k -> new HashMap<>());
            int currentDuration = yearSummary.getOrDefault(monthName, 0);

            yearSummary.put(monthName, currentDuration + training.getDuration());
        }

        return monthlySummary;
    }

    public void deleteByUsernameAndDateAndDuration(String username, LocalDate date, int duration) {
        log.debug("Delete training by trainer with username = {} and date = {} and duration = {}", username, date, duration);
        if (trainingRepository.deleteTrainingByDateAndDuration(username, date, duration) == 0) {
            log.debug("Training not found");
        }
    }
}
