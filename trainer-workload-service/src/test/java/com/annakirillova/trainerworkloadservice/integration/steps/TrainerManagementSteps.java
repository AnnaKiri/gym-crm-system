package com.annakirillova.trainerworkloadservice.integration.steps;

import com.annakirillova.common.dto.ActionType;
import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.common.dto.TrainingInfoDto;
import com.annakirillova.trainerworkloadservice.integration.TrainerMessageSender;
import com.annakirillova.trainerworkloadservice.model.TrainerSummary;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.USERNAME_SUMMARY;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerManagementSteps extends BaseSteps {

    private static final String REST_URL_SLASH = REST_URL + '/';
    private static final long DELAY_MS = 1000;
    private static TrainerSummary trainerSummary;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TrainerMessageSender trainerMessageSender;

    private ResultActions resultActions;
    private TrainingDto trainingDto;

    @Given("a trainer with username {string} and summary data is saved")
    public void a_trainer_with_username_and_summary_data_is_saved(String username) {
        trainerSummary = USERNAME_SUMMARY.get(username);
        mongoTemplate.getDb().drop();
        mongoTemplate.save(trainerSummary);
    }

    @When("the user {string} retrieves the monthly summary for trainer {string}")
    public void the_user_retrieves_the_monthly_summary_for_trainer(String usernameForLogin, String trainerUsername) throws Exception {
        resultActions = perform(get(REST_URL_SLASH + trainerUsername + "/monthly-summary")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAccessToken(usernameForLogin)))
                .andDo(print());
    }

    @Then("the monthly summary should be returned")
    public void the_monthly_summary_should_be_returned() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_MATCHER_WITH_SUMMARY_LIST.contentJson(trainerSummary));
    }

    @When("the user {string} attempts to retrieve the monthly summary for nonexistent trainer {string}")
    public void the_user_attempts_to_retrieve_the_monthly_summary_for_nonexistent_trainer(String usernameForLogin, String trainerUsername) throws Exception {
        resultActions = perform(get(REST_URL_SLASH + trainerUsername + "/monthly-summary")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAccessToken(usernameForLogin)))
                .andDo(print());
    }

    @Then("the request should return not found status")
    public void the_request_should_return_not_found_status() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

    @Given("a new training with name {string}, type ID {int}, date {string}, duration {int}, trainee username {string}, and trainer username {string}")
    public void a_new_training_with_name(String name, int typeId, String date, int duration, String traineeUsername, String trainerUsername) {
        trainingDto = new TrainingDto();
        trainingDto.setName(name);
        trainingDto.setTypeId(typeId);
        trainingDto.setDate(LocalDate.parse(date));
        trainingDto.setDuration(duration);
        trainingDto.setTraineeUsername(traineeUsername);
        trainingDto.setTrainerUsername(trainerUsername);
    }

    @When("somebody {string} the training")
    public void the_user_do_smth_with_new_training(String action) throws Exception {
        ActionType actiontype = ActionType.valueOf(action);
        TrainingInfoDto trainingInfoDto = TrainingInfoDto.builder()
                .username(trainingDto.getTrainerUsername())
                .firstName(trainerSummary.getFirstName())
                .lastName(trainerSummary.getLastName())
                .isActive(trainerSummary.getIsActive())
                .date(trainingDto.getDate())
                .duration(trainingDto.getDuration())
                .actionType(actiontype)
                .build();
        trainerMessageSender.sendMessage(trainingInfoDto);

        Thread.sleep(DELAY_MS);
    }

    @Then("the monthly summary for date {string} should be {string} by {int} minutes")
    public void the_montly_summary_should_increased_decreased_by_some_minutes(String date, String action, int duration) throws Exception {
        LocalDate localDate = LocalDate.parse(date);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        TrainerSummary trainerSummaryUpdated = TRAINER_MATCHER_WITH_SUMMARY_LIST.readFromJson(resultActions);
        int durationForDateUpdated = trainerSummaryUpdated.getSummaryList()
                .stream()
                .filter(el -> el.getYear() == localDate.getYear())
                .filter(el -> el.getMonth() == localDate.getMonthValue())
                .mapToInt(TrainerSummaryDto.Summary::getDuration)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Duration for the specified date not found for updated summary"));

        int durationForDateInitial = trainerSummary.getSummaryList()
                .stream()
                .filter(el -> el.getYear() == localDate.getYear())
                .filter(el -> el.getMonth() == localDate.getMonthValue())
                .mapToInt(TrainerSummaryDto.Summary::getDuration)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Duration for the specified date not found for initial summary"));

        int expectedDuration = action.equals("increased")
                ? durationForDateInitial + duration
                : durationForDateInitial - duration;
        assertEquals(expectedDuration, durationForDateUpdated);
    }
}
