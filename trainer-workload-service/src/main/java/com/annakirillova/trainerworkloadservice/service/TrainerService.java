package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    @Transactional
    public Trainer create(String firstName, String lastName, String username, boolean isActive) {
        log.debug("Check trainer");
        Optional<Trainer> receivedTrainer = trainerRepository.findByUsername(username);

        if (receivedTrainer.isPresent()) {
            return receivedTrainer.get();
        }

        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setIsActive(isActive);
        trainer.setSummaryList(new ArrayList<>());

        log.debug("Create new trainer");
        return trainerRepository.save(trainer);
    }

    public Trainer get(String username) {
        log.debug("Get trainer with username = {}", username);
        return trainerRepository.getTrainerIfExists(username);
    }

    public Trainer addOrUpdateTrainingDuration(String username, LocalDate date, int duration) {
        log.debug("Add or update training duration {} for trainer with username = {} and date = {}", duration, username, date);
        Trainer trainer = trainerRepository.getTrainerIfExists(username);

        Optional<Trainer.Summary> existingSummary = trainer.getSummaryList().stream()
                .filter(summary -> summary.getYear() == date.getYear() && summary.getMonth() == date.getMonthValue())
                .findFirst();

        if (existingSummary.isPresent()) {
            Trainer.Summary summary = existingSummary.get();
            summary.setDuration(summary.getDuration() + duration);
            log.debug("Updated training duration for trainer with username = {} for month = {} and year = {}", username, date.getMonthValue(), date.getYear());
        } else {
            Trainer.Summary newSummary = new Trainer.Summary(date.getYear(), date.getMonthValue(), duration);
            trainer.getSummaryList().add(newSummary);
            log.debug("Added new training summary for trainer with username = {} for month = {} and year = {}", username, date.getMonthValue(), date.getYear());
        }

        return trainerRepository.save(trainer);
    }

    public Trainer deleteTrainingDurationFromSummaryByDateAndUsername(String username, LocalDate date, int duration) {
        log.debug("Delete training duration {} for trainer with username = {} and date = {}", duration, username, date);
        Trainer trainer = trainerRepository.getTrainerIfExists(username);

        trainer.getSummaryList().stream()
                .filter(summary -> summary.getYear() == date.getYear() && summary.getMonth() == date.getMonthValue())
                .findFirst()
                .ifPresent(summary -> {
                    summary.setDuration(Math.max(0, summary.getDuration() - duration));
                    log.debug("Training duration for trainer with username = {} for month = {} and year = {} was updated", username, date.getMonthValue(), date.getYear());
                });

        return trainerRepository.save(trainer);
    }
}
