package com.annakirillova.trainerworkloadservice.model;

import com.annakirillova.common.dto.TrainerSummaryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trainers")
@CompoundIndexes({
        @CompoundIndex(name = "name_idx", def = "{'firstName': 1, 'lastName': 1}")
})
@NoArgsConstructor
public class TrainerSummaryMongoDb extends TrainerSummaryDto {

    public TrainerSummaryMongoDb(String username, String firstName, String lastName, String isActive, List<Summary> summaryList) {
        super(username, firstName, lastName, isActive, summaryList);
    }

    @Id
    @NotBlank
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @NotBlank
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @NotNull
    public String getIsActive() {
        return super.getIsActive();
    }
}
