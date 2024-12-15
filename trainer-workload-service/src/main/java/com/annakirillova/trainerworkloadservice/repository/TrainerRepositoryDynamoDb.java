package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummaryDynamoDb;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
@Profile("dev")
@RequiredArgsConstructor
public class TrainerRepositoryDynamoDb implements TrainerRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    @Override
    public Optional<TrainerSummaryDto> findByUsernameSpecial(String username) {
        Key key = Key.builder()
                .partitionValue(username)
                .sortValue("true")
                .build();
        TrainerSummaryDynamoDb result = dynamoDbTemplate.load(key, TrainerSummaryDynamoDb.class);
        return Optional.ofNullable(result);
    }

    @Override
    public TrainerSummaryDto getTrainerIfExists(String username) {
        return TrainerRepository.super.getTrainerIfExists(username);
    }

    @Override
    public TrainerSummaryDto save(TrainerSummaryDto trainerSummary) {
        TrainerSummaryDynamoDb dynamoDbEntity = new TrainerSummaryDynamoDb(trainerSummary.getUsername(),
                                                                            trainerSummary.getFirstName(),
                                                                            trainerSummary.getLastName(),
                                                                            trainerSummary.getIsActive(),
                                                                            trainerSummary.getSummaryList());
        dynamoDbTemplate.save(dynamoDbEntity);
        return dynamoDbEntity;
    }
}
