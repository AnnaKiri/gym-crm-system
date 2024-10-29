package com.annakirillova.trainerworkloadservice.integration.steps;

import com.annakirillova.trainerworkloadservice.TestData;
import com.annakirillova.trainerworkloadservice.model.TrainerSummary;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.annakirillova.trainerworkloadservice.TestData.USERNAME_SUMMARY;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerManagementSteps extends BaseSteps {

    private static final String REST_URL_SLASH = REST_URL + '/';
    private static TrainerSummary trainerSummary;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ResultActions resultActions;

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
                .andExpect(TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST.contentJson(trainerSummary));
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
}
