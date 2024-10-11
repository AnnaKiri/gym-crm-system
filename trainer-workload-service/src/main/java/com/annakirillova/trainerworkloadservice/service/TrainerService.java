package com.annakirillova.trainerworkloadservice.service;

import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        trainer.setActive(isActive);

        log.debug("Create new trainer");
        return trainerRepository.save(trainer);
    }

    public Trainer get(String username) {
        log.debug("Get trainer with username = {}", username);
        return trainerRepository.getTrainerIfExists(username);
    }
}
