package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummaryMongoDb;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Profile("!dev")
public interface TrainerRepositoryMongoDb extends MongoRepository<TrainerSummaryMongoDb, String>, TrainerRepository {

    Optional<TrainerSummaryMongoDb> findByUsername(String username);

    @Override
    default Optional<TrainerSummaryDto> findByUsernameSpecial(String username) {
        return findByUsername(username).map(trainer -> trainer);
    }
}
