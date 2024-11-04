package com.annakirillova.crmsystem.integration.steps;

import com.annakirillova.common.dto.UserDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_LIST;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.trainingtype.TrainingTypeController.REST_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingTypeManagementSteps extends BaseSteps {

    @Given("some existing user with username {string}")
    public void some_existing_user_for_training_type_with_username(String username) {
        userDto = UserDto.builder().username(username).build();
    }

    @When("the user retrieves the list of training types")
    public void the_user_retrieves_the_list_of_training_types() throws Exception {
        resultActions = perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the list of training types should be returned")
    public void the_list_of_training_types_should_be_returned() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TYPE_MATCHER.contentJson(TRAINING_TYPE_LIST));
    }
}
