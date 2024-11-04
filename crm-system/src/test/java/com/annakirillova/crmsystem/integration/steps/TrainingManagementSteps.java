package com.annakirillova.crmsystem.integration.steps;

import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TrainingTestData;
import com.annakirillova.crmsystem.util.JsonUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_ID_TO_TRAINING;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.training.TrainingController.REST_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingManagementSteps extends BaseSteps {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Given("an existing user with username {string}")
    public void an_existing_user_with_username(String username) {
        userDto = UserDto.builder().username(username).build();
    }

    @When("the user creates the training")
    public void a_new_training_with_valid_details() throws Exception {
        TrainingDto newTrainingDto = TrainingTestData.getNewTrainingDto();
        resultActions = perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTrainingDto)))
                .andDo(print());
    }

    @Then("the training should be created successfully")
    public void the_training_should_be_created_successfully() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @When("the user retrieves the training details with ID {int}")
    public void the_user_retrieves_the_training_details(int trainingId) throws Exception {
        resultActions = perform(get(REST_URL_SLASH + trainingId)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the training details for ID {int} should be returned")
    public void the_training_details_should_be_returned(int trainingId) throws Exception {
        TrainingDto trainingDto = TRAINING_ID_TO_TRAINING.get(trainingId);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(trainingDto));
    }

    @When("the user attempts to retrieve a nonexistent training")
    public void the_trainer_attempts_to_retrieve_a_nonexistent_training() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + 9999)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the training request should return not found status")
    public void the_training_request_should_return_not_found_status() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }
}
