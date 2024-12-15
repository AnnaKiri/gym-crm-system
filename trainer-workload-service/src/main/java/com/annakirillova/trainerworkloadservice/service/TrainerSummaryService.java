package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummaryMongoDb;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerSummaryService {

    private final TrainerRepository trainerRepository;

    public TrainerSummaryDto create(String firstName, String lastName, String username, boolean isActive) {
        log.debug("Check trainer");
        Optional<TrainerSummaryDto> receivedTrainer = trainerRepository.findByUsernameSpecial(username);

        if (receivedTrainer.isPresent()) {
            return receivedTrainer.get();
        }

        TrainerSummaryMongoDb trainerSummaryMongoDb = new TrainerSummaryMongoDb();
        trainerSummaryMongoDb.setUsername(username);
        trainerSummaryMongoDb.setFirstName(firstName);
        trainerSummaryMongoDb.setLastName(lastName);
        trainerSummaryMongoDb.setIsActive(String.valueOf(isActive));
        trainerSummaryMongoDb.setSummaryList(new ArrayList<>());

        log.debug("Create new trainer");
        return trainerRepository.save(trainerSummaryMongoDb);
    }

    public TrainerSummaryDto get(String username) {
        log.debug("Get trainer with username = {}", username);
        return trainerRepository.getTrainerIfExists(username);
    }

    @Transactional
    public TrainerSummaryDto addOrUpdateTrainingDuration(String username, LocalDate date, int duration) {
        log.debug("Add or update training duration {} for trainer with username = {} and date = {}", duration, username, date);
        TrainerSummaryDto trainerSummary = trainerRepository.getTrainerIfExists(username);

        Optional<TrainerSummaryDto.Summary> existingSummary = getExistingSummary(trainerSummary, date);

        if (existingSummary.isPresent()) {
            TrainerSummaryDto.Summary summary = existingSummary.get();
            summary.setDuration(summary.getDuration() + duration);
            log.debug("Updated training duration for trainer with username = {} for month = {} and year = {}", username, date.getMonthValue(), date.getYear());
        } else {
            TrainerSummaryDto.Summary newSummary = new TrainerSummaryDto.Summary(date.getYear(), date.getMonthValue(), duration);
            trainerSummary.getSummaryList().add(newSummary);
            log.debug("Added new training summary for trainer with username = {} for month = {} and year = {}", username, date.getMonthValue(), date.getYear());
        }

        return trainerRepository.save(trainerSummary);
    }

    public TrainerSummaryDto deleteTrainingDurationFromSummaryByDateAndUsername(String username, LocalDate date, int duration) {
        log.debug("Delete training duration {} for trainer with username = {} and date = {}", duration, username, date);
        TrainerSummaryDto trainerSummary = trainerRepository.getTrainerIfExists(username);

        getExistingSummary(trainerSummary, date)
                .ifPresent(summary -> {
                    summary.setDuration(Math.max(0, summary.getDuration() - duration));
                    log.debug("Training duration for trainer with username = {} for month = {} and year = {} was updated", username, date.getMonthValue(), date.getYear());
                });

        return trainerRepository.save(trainerSummary);
    }

    static Optional<TrainerSummaryDto.Summary> getExistingSummary(TrainerSummaryDto trainerSummary, LocalDate date) {
        return trainerSummary.getSummaryList().stream()
                .filter(summary -> Objects.equals(summary.getYear(), date.getYear())
                        && Objects.equals(summary.getMonth(), date.getMonthValue()))
                .findFirst();
    }
}
