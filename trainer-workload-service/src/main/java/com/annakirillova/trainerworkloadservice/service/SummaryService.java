package com.annakirillova.trainerworkloadservice.service;


import com.annakirillova.trainerworkloadservice.model.Summary;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.repository.SummaryRepository;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final TrainerRepository trainerRepository;

    public List<Summary> getMonthlyTrainingSummary(String username) {
        log.debug("Get monthly summary for trainer with username = {}", username);
        return summaryRepository.findAllByTrainerUsername(username);
    }

    public void deleteTrainingDurationFromSummaryByDateAndUsername(String username, LocalDate date, int duration) {
        log.debug("Delete training duration {} from summary by trainer with username = {} and date = {}", duration, username, date);
        if (summaryRepository.deleteTrainingDurationFromSummaryByDateAndUsername(username, date.getYear(), date.getMonthValue(), duration) == 0) {
            log.debug("Training duration was not deleted");
        }
    }

    public void addOrUpdateTrainingDuration(String username, LocalDate date, int duration) {
        log.debug("Add training duration {} to summary by trainer with username = {} and date = {}", duration, username, date);
        Optional<Summary> summaryOpt = summaryRepository.findByTrainerAndDate(username, date.getYear(), date.getMonthValue());

        if (summaryOpt.isPresent()) {
            Summary summary = summaryOpt.get();
            summary.setDuration(summary.getDuration() + duration);
            summaryRepository.save(summary);
            log.debug("Training duration {} for trainer with username = {} and date = {} was updated", duration, username, date);
        } else {
            Trainer trainer = trainerRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));
            Summary newSummary = new Summary(null, trainer, date.getYear(), date.getMonthValue(), duration);
            summaryRepository.save(newSummary);
            log.debug("Training duration {} for trainer with username = {} and date = {} was added", duration, username, date);

        }
    }
}
