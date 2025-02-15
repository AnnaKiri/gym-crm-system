package com.annakirillova.trainerworkloadservice.model;

import com.annakirillova.common.dto.TrainerSummaryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trainers")
@EqualsAndHashCode(of = "username")
@CompoundIndexes({
        @CompoundIndex(name = "name_idx", def = "{'firstName': 1, 'lastName': 1}")
})
public class TrainerSummary {

    @Id
    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean isActive;

    @NotNull
    private List<TrainerSummaryDto.Summary> summaryList;
}
