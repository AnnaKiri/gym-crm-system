package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.error.NotFoundException;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    Optional<Trainer> findByUsername(String username);

    default Trainer getTrainerIfExists(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer with username=" + username + " not found"));
    }
}
