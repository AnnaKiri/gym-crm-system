package com.annakirillova.trainerworkloadservice.web;

import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import com.annakirillova.trainerworkloadservice.model.Summary;
import com.annakirillova.trainerworkloadservice.model.Trainer;
import com.annakirillova.trainerworkloadservice.service.SummaryService;
import com.annakirillova.trainerworkloadservice.service.TrainerService;
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
public class TrainerController {

    static final String REST_URL = "/trainers";

    private final TrainerService trainerService;
    private final SummaryService summaryService;

    @GetMapping("/{username}/monthly_summary")
    @Transactional
    public TrainerDto get(@PathVariable String username) {
        log.debug("Get the monthly summary for the trainee with username={}", username);
        Trainer receivedTrainer = trainerService.get(username);
        List<Summary> summaryList = summaryService.getMonthlyTrainingSummary(username);

        return createDtoWithMonthlySummary(receivedTrainer, getDtos(summaryList));
    }
}
