package com.annakirillova.trainerworkloadservice.web.trainer;

import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = TrainerController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Trainer Controller", description = "Managing gym trainers")
public class TrainerController {

    static final String REST_URL = "/trainers";

    private final TrainerService trainerService;

    @GetMapping("/{username}/monthly-summary")
    @Operation(summary = "Get trainer details", description = "Gets the summary of the specified trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public Trainer get(@PathVariable String username) {
        log.debug("Get the monthly summary for the trainee with username={}", username);
        return trainerService.get(username);
    }
}
