package com.annakirillova.crmsystem.integration.steps;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TraineeDto;
import com.annakirillova.common.dto.TrainerDto;
import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TraineeTestData;
import com.annakirillova.crmsystem.util.JsonUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO_MATCHER;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.USERNAMES_TO_TRAINEE_DTO;
import static com.annakirillova.crmsystem.TraineeTestData.getUpdatedTraineeDto;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainerTestData.USERNAMES_TO_FREE_TRAINERS_LIST;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.USERNAMES_TO_TRAINEE_TRAININGS_LIST;
import static com.annakirillova.crmsystem.UserTestData.USERS_PASSWORDS;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_6;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.trainee.TraineeController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeManagementSteps extends BaseSteps {

    private static final String REST_URL_SLASH = REST_URL + '/';

    private TraineeDto traineeDto;

    @Given("a new trainee with first name {string}, last name {string}, birthday {string} and address {string}")
    public void a_new_trainee_with_name(String firstName, String lastName, String birthdayString, String address) {
        LocalDate birthday = LocalDate.parse(birthdayString);
        traineeDto = TraineeTestData.getNewTraineeDto();
        traineeDto.setFirstName(firstName);
        traineeDto.setLastName(lastName);
        traineeDto.setBirthday(birthday);
        traineeDto.setAddress(address);
    }

    @When("the trainee registers")
    public void the_trainee_registers() throws Exception {
        resultActions = perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto)))
                .andDo(print());
    }

    @Then("the trainee should be created with a username {string}")
    public void the_trainee_should_be_created(String username) throws Exception {
        resultActions
                .andExpect(status()
                        .isCreated());
        LoginRequestDto created = LOGIN_REQUEST_DTO_MATCHER.readFromJson(resultActions);
        assertEquals(username, created.getUsername());

        //put new trainee in maps
        USERS_PASSWORDS.put(username, created.getPassword());
        traineeDto.setUsername(username);
        TraineeDto newTraineeDto = new TraineeDto(traineeDto);
        USERNAMES_TO_TRAINEE_DTO.put(username, newTraineeDto);
    }

    @Given("an existing trainee with username {string}")
    public void an_existing_trainee_with_username(String username) {
        userDto = UserDto.builder().username(username).build();
    }

    @When("the trainee changes password to {string}")
    public void the_trainee_changes_password(String newPassword) throws Exception {
        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername() + "/password")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andDo(print());
    }

    @Then("the trainee password change should succeed")
    public void the_password_change_should_succeed() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @When("the trainee retrieves their details")
    public void the_trainee_retrieves_details() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the trainee details should be returned")
    public void the_trainee_details_should_be_returned() throws Exception {
        TraineeDto expected = USERNAMES_TO_TRAINEE_DTO.get(userDto.getUsername());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(expected));
    }

    @When("the trainee updates their details")
    public void the_trainee_updates_details() throws Exception {
        TraineeDto updatedTrainee = getUpdatedTraineeDto();

        TraineeDto updateInfo = TraineeDto.builder()
                .firstName(updatedTrainee.getFirstName())
                .lastName(updatedTrainee.getLastName())
                .birthday(updatedTrainee.getBirthday())
                .address(updatedTrainee.getAddress())
                .isActive(updatedTrainee.getIsActive())
                .build();

        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updateInfo)))
                .andDo(print());
    }

    @Then("the trainee details should be updated successfully")
    public void the_trainee_details_should_be_updated() throws Exception {
        TraineeDto traineeReference = USERNAMES_TO_TRAINEE_DTO.get(userDto.getUsername());

        TraineeDto traineeExpected = getUpdatedTraineeDto();
        traineeExpected.setUsername(traineeReference.getUsername());
        traineeExpected.setId(traineeReference.getId());
        traineeExpected.setTrainerList(traineeReference.getTrainerList());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeExpected));
    }

    @When("the trainee attempts to delete their account")
    public void the_trainee_attempts_to_delete_account() throws Exception {
        resultActions = perform(delete(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the trainee should be deleted successfully")
    public void the_trainee_should_be_deleted() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @When("the trainee retrieves free trainers")
    public void the_trainee_retrieves_free_trainers() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + userDto.getUsername() + "/free-trainers")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the list of free trainers should be returned")
    public void the_list_of_free_trainers_should_be_returned() throws Exception {
        List<TrainerDto> freeTrainers = USERNAMES_TO_FREE_TRAINERS_LIST.get(userDto.getUsername());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(freeTrainers));
    }

    @When("the trainee retrieves their trainings")
    public void the_trainee_retrieves_trainings() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + userDto.getUsername() + "/trainings")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the list of trainings for trainee should be returned")
    public void the_list_of_trainings_for_trainee_should_be_returned() throws Exception {
        List<TrainingDto> trainings = USERNAMES_TO_TRAINEE_TRAININGS_LIST.get(userDto.getUsername());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(trainings));
    }

    @When("the trainee sets their account status to {string}")
    public void the_trainee_sets_account_status(String isActive) throws Exception {
        resultActions = perform(patch(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .param("isActive", isActive))
                .andDo(print());
    }

    @Then("the trainee account status should be updated successfully")
    public void the_account_status_should_be_updated() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @When("the trainee attempts to retrieve a nonexistent trainee")
    public void the_trainee_attempts_to_retrieve_nonexistent() throws Exception {
        resultActions = perform(get(REST_URL_SLASH + "Not.Found")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken()))
                .andDo(print());
    }

    @Then("the request should return trainee not found status")
    public void the_request_should_return_not_found() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

    @When("the trainee attempts to update their details with invalid data")
    public void the_trainee_attempts_to_update_with_invalid_data() throws Exception {
        TraineeDto invalidTraineeDto = TraineeDto.builder().build();
        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidTraineeDto)))
                .andDo(print());
    }

    @Then("the trainee update request should return bad request status")
    public void the_update_request_should_return_bad_request() throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    @When("the trainee updates their trainer list")
    public void the_trainee_updates_trainer_list() throws Exception {
        List<String> trainerUsernames = List.of(USER_5.getUsername(), USER_6.getUsername());
        resultActions = perform(put(REST_URL_SLASH + userDto.getUsername() + "/trainers")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerUsernames)))
                .andDo(print());
    }

    @Then("the trainer list should be updated successfully")
    public void the_trainer_list_should_be_updated() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(List.of(TRAINER_DTO_1, TRAINER_DTO_2)));
    }

    @When("the trainee attempts to set their account status again to {string}")
    public void the_trainee_attempts_to_set_account_status_again(String isActive) throws Exception {
        resultActions = perform(patch(REST_URL_SLASH + userDto.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(userDto).getAccessToken())
                .param("isActive", isActive))
                .andDo(print());
    }

    @Then("the trainee set active request should return conflict status")
    public void the_request_should_return_conflict() throws Exception {
        resultActions.andExpect(status().isConflict());
    }

}
