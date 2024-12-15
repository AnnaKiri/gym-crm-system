package com.annakirillova.trainerworkloadservice.model;

import com.annakirillova.common.dto.TrainerSummaryDto;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
@NoArgsConstructor
public class TrainerSummaryDynamoDb extends TrainerSummaryDto {

    public TrainerSummaryDynamoDb(String username, String firstName, String lastName, String isActive, List<Summary> summaryList) {
        super(username, firstName, lastName, isActive, summaryList);
    }

    @DynamoDbPartitionKey
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @DynamoDbSortKey
    @Override
    public String getIsActive() {
        return super.getIsActive();
    }

    @DynamoDbConvertedBy(SummaryListConverter.class)
    @Override
    public List<TrainerSummaryDto.Summary> getSummaryList() {
        return super.getSummaryList();
    }
}
