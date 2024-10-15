package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.exception.NotFoundException;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerRepository extends MongoRepository<Trainer, String> {

    Optional<Trainer> findByUsername(String username);

    default Trainer getTrainerIfExists(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer with username=" + username + " not found"));
    }
}
