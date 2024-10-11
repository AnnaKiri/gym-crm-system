package com.annakirillova.trainerworkloadservice.web.trainer;

import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import com.annakirillova.trainerworkloadservice.model.Summary;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.service.SummaryService;
import com.annakirillova.trainerworkloadservice.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.annakirillova.trainerworkloadservice.util.SummaryUtil.getDtos;
import static com.annakirillova.trainerworkloadservice.util.TrainerUtil.createDtoWithMonthlySummary;

@RestController
@RequestMapping(value = TrainerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Trainer Controller", description = "Managing gym trainers")
public class TrainerController {

    static final String REST_URL = "/trainers";

    private final TrainerService trainerService;
    private final SummaryService summaryService;

    @GetMapping("/{username}/monthly-summary")
    @Transactional
    @Operation(summary = "Get trainer details", description = "Gets the summary of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public TrainerDto get(@PathVariable String username) {
        log.debug("Get the monthly summary for the trainee with username={}", username);
        Trainer receivedTrainer = trainerService.get(username);
        List<Summary> summaryList = summaryService.getMonthlyTrainingSummary(username);

        return createDtoWithMonthlySummary(receivedTrainer, getDtos(summaryList));
    }
}
