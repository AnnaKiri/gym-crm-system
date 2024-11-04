package com.annakirillova.crmsystem.integration.steps;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TrainerDto;
import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TrainerTestData;
import com.annakirillova.crmsystem.util.JsonUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST;
import static com.annakirillova.crmsystem.TrainerTestData.USERNAMES_TO_TRAINER_DTO;
import static com.annakirillova.crmsystem.TrainerTestData.getUpdatedTrainerDto;
import static com.annakirillova.crmsystem.TrainerTestData.jsonWithSpecializationId;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.USERNAMES_TO_TRAINEE_TRAININGS_LIST;
import static com.annakirillova.crmsystem.UserTestData.USERS_PASSWORDS;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.trainer.TrainerController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerManagementSteps extends BaseSteps {

    private static final String REST_URL_SLASH = REST_URL + '/';

    private TrainerDto trainerDto;

    @Given("a new trainer with first name {string}, last name {string}, specialization ID {int}")
    public void a_new_trainer_with_name(String firstName, String lastName, int specializationId) {
        trainerDto = TrainerTestData.getNewTrainerDto();
        trainerDto.setFirstName(firstName);
        trainerDto.setLastName(lastName);
        trainerDto.setSpecializationId(specializationId);
    }

    @When("the trainer registers")
    public void the_trainer_registers() throws Exception {
        resultActions = perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(trainerDto, trainerDto.getSpecializationId())))
                .andDo(print());
    }

    @Then("the trainer should be created with a username {string}")
    public void the_trainer_should_be_created(String username) throws Exception {
        resultActions
                .andExpect(status().isCreated());
        LoginRequestDto created = LOGIN_REQUEST_DTO_MATCHER.readFromJson(resultActions);
        assertEquals(username, created.getUsername());

        //put new trainer in a map
        USERS_PASSWORDS.put(username, created.getPassword());
        trainerDto.setUsername(username);
        TrainerDto newTrainerDto = new TrainerDto(trainerDto);
        USERNAMES_TO_TRAINER_DTO.put(username, newTrainerDto);
    }

    @Given("an existing trainer with username {string}")
    public void an_existing_trainer_with_username(String username) {
        userDto = UserDto.builder().username(username).build();
    }

    @When("the trainer changes password to {string}")
    public void the_trainer_changes_password(String newPassword) throws Exception {
        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername() + "/password")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andDo(print());
    }

    @Then("the trainer password change should succeed")
    public void the_password_change_should_succeed() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @When("the trainer retrieves their details")
    public void the_trainer_retrieves_details() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the trainer details should be returned")
    public void the_trainer_details_should_be_returned() throws Exception {
        TrainerDto expected = USERNAMES_TO_TRAINER_DTO.get(userDto.getUsername());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(expected));
    }

    @When("the trainer updates their details")
    public void the_trainer_updates_details() throws Exception {
        TrainerDto updatedTrainer = getUpdatedTrainerDto();

        TrainerDto updateInfo = TrainerDto.builder()
                .firstName(updatedTrainer.getFirstName())
                .lastName(updatedTrainer.getLastName())
                .specializationId(updatedTrainer.getSpecializationId())
                .isActive(updatedTrainer.getIsActive())
                .build();

        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(updateInfo, updateInfo.getSpecializationId())))
                .andDo(print());
    }

    @Then("the trainer details should be updated successfully")
    public void the_trainer_details_should_be_updated() throws Exception {
        TrainerDto trainerReference = USERNAMES_TO_TRAINER_DTO.get(userDto.getUsername());

        TrainerDto trainerExpected = getUpdatedTrainerDto();
        trainerExpected.setUsername(trainerReference.getUsername());
        trainerExpected.setTraineeList(trainerReference.getTraineeList());
        trainerExpected.setId(trainerReference.getId());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(trainerExpected));
    }

    @When("the trainer retrieves their trainings")
    public void the_trainer_retrieves_trainings() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + userDto.getUsername() + "/trainings")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the list of trainings for trainer should be returned")
    public void the_list_of_trainings_for_trainer_should_be_returned() throws Exception {
        List<TrainingDto> trainings = USERNAMES_TO_TRAINEE_TRAININGS_LIST.get(userDto.getUsername());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(trainings));
    }

    @When("the trainer sets their account status to {string}")
    public void the_trainer_sets_account_status(String isActive) throws Exception {
        resultActions = perform(patch(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .param("isActive", isActive))
                .andDo(print());
    }

    @Then("the trainer account status should be updated successfully")
    public void the_account_status_should_be_updated() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @When("the trainer attempts to retrieve a nonexistent trainer")
    public void the_trainer_attempts_to_retrieve_nonexistent() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + "Not.Found")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the request should return trainer not found status")
    public void the_request_should_return_not_found() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

    @When("the trainer attempts to update their details with invalid data")
    public void the_trainer_attempts_to_update_with_invalid_data() throws Exception {
        TrainerDto invalidTrainerDto = TrainerDto.builder().build();
        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidTrainerDto)))
                .andDo(print());
    }

    @Then("the trainer update request should return bad request status")
    public void the_update_request_should_return_bad_request() throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    @When("the trainer attempts to set their account status again to {string}")
    public void the_trainer_attempts_to_set_account_status_again(String isActive) throws Exception {
        resultActions = perform(patch(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .param("isActive", isActive))
                .andDo(print());
    }

    @Then("the trainer set active request should return conflict status")
    public void the_request_should_return_conflict() throws Exception {
        resultActions.andExpect(status().isConflict());
    }
}
