package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.exception.NotFoundException;
import com.annakirillova.trainerworkloadservice.model.TrainerSummary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Profile("!dev")
public interface TrainerRepository extends MongoRepository<TrainerSummary, String> {

    Optional<TrainerSummary> findByUsername(String username);

    default TrainerSummary getTrainerIfExists(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer with username=" + username + " not found"));
    }
}
