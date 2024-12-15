package com.annakirillova.trainerworkloadservice.model;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;

@NoArgsConstructor
public class SummaryListConverter implements AttributeConverter<List<TrainerSummaryDto.Summary>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(List<TrainerSummaryDto.Summary> summaries) {
        try {
            String json = objectMapper.writeValueAsString(summaries);
            return AttributeValue.builder().s(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Summary list to JSON", e);
        }
    }

    @Override
    public List<TrainerSummaryDto.Summary> transformTo(AttributeValue attributeValue) {
        try {
            String json = attributeValue.s();
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TrainerSummaryDto.Summary.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Summary list", e);
        }
    }

    @Override
    public EnhancedType<List<TrainerSummaryDto.Summary>> type() {
        return EnhancedType.listOf(TrainerSummaryDto.Summary.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
