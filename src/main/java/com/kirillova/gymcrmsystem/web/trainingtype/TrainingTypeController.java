package com.kirillova.gymcrmsystem.web.trainingtype;

import com.kirillova.gymcrmsystem.dao.TrainingTypeDAO;
import com.kirillova.gymcrmsystem.models.TrainingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TrainingTypeController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Training Type Controller", description = "Managing training types")
public class TrainingTypeController {

    static final String REST_URL = "/training-types";

    private final TrainingTypeDAO trainingTypeDAO;

    @GetMapping
    @Operation(summary = "Get training types", description = "Gets the list of all training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training types retrieved successfully")
    })
    public List<TrainingType> get() {
        log.debug("Get training types");
        return trainingTypeDAO.getAll();
    }
}
