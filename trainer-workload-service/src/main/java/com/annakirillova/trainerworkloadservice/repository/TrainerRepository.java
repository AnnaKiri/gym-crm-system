package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.exception.NotFoundException;

import java.util.Optional;

public interface TrainerRepository {

    Optional<TrainerSummaryDto> findByUsernameSpecial(String username);

    TrainerSummaryDto save(TrainerSummaryDto trainerSummary);

    default TrainerSummaryDto getTrainerIfExists(String username) {
        return findByUsernameSpecial(username)
                .orElseThrow(() -> new NotFoundException("Trainer with username=" + username + " not found"));
    }
}
