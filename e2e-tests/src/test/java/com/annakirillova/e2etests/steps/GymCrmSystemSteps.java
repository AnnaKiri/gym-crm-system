package com.annakirillova.e2etests.steps;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.common.dto.TraineeDto;
import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.e2etests.feign.CrmSystemFeignClient;
import com.annakirillova.e2etests.feign.TrainerWorkloadServiceFeignClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.annakirillova.e2etests.TestData.USERS_PASSWORDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GymCrmSystemSteps {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private CrmSystemFeignClient crmSystemFeignClient;

    @Autowired
    private TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

    private TraineeDto traineeDto;
    private ResponseEntity<LoginRequestDto> registrationResponse;
    private TokenResponseDto tokenResponse;
    private String accessToken;
    private LoginRequestDto loginRequestDto;
    private TrainingDto trainingDto;
    private Exception exception;

    @Given("a new trainee with first name {string}, last name {string}, birthday {string}, and address {string}")
    public void a_new_trainee_with_name(String firstName, String lastName, String birthday, String address) {
        traineeDto = new TraineeDto();
        traineeDto.setFirstName(firstName);
        traineeDto.setLastName(lastName);
        traineeDto.setBirthday(LocalDate.parse(birthday));
        traineeDto.setAddress(address);
        traineeDto.setIsActive(true);
    }

    @When("the trainee registers")
    public void the_trainee_registers() throws Exception {
        registrationResponse = crmSystemFeignClient.register(traineeDto);
    }

    @Then("the trainee should be created with a username {string}")
    public void the_trainee_should_be_created(String username) throws Exception {
        assertEquals(201, registrationResponse.getStatusCodeValue());
        assertEquals(username, registrationResponse.getBody().getUsername());
    }

    @Given("an existing trainee with username {string} and password {string}")
    public void set_trainee_credentials(String username, String password) {
        loginRequestDto = new LoginRequestDto(username, password);
    }

    @When("the trainee authenticates")
    public void authenticate() {
        tokenResponse = crmSystemFeignClient.authenticate(loginRequestDto);
    }

    @Then("the access token should be available")
    public void check_access_token_available() {
        assertNotNull(tokenResponse);
        accessToken = tokenResponse.getAccessToken();
        assertNotNull(accessToken);
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

    @When("the trainee with username {string} creates a training")
    public void create_training(String username) throws InterruptedException {
        accessToken = getAccessToken(username);
        crmSystemFeignClient.createTraining(BEARER_PREFIX + accessToken, trainingDto);

        Thread.sleep(1000);
    }

    @Then("the trainer's monthly summary should match expected summary for {string}")
    public void check_trainer_summary(String trainerUsername) {
        TrainerSummaryDto.Summary summary = new TrainerSummaryDto.Summary(
                trainingDto.getDate().getYear(),
                trainingDto.getDate().getMonthValue(),
                trainingDto.getDuration());

        List<TrainerSummaryDto.Summary> summaryList = new ArrayList<>();
        summaryList.add(summary);

        TrainerSummaryDto actualSummaryDto = trainerWorkloadServiceFeignClient.getMonthlySummary(BEARER_PREFIX + accessToken, trainerUsername);
        assertNotNull(actualSummaryDto);
        assertEquals(trainingDto.getTrainerUsername(), actualSummaryDto.getUsername());
        assertEquals(summaryList, actualSummaryDto.getSummaryList());
    }

    @When("the trainee with username {string} deletes trainee with username {string}")
    public void delete_trainee(String authUserUsername, String traineeUsername) throws InterruptedException {
        accessToken = getAccessToken(authUserUsername);
        crmSystemFeignClient.deleteTrainee(BEARER_PREFIX + accessToken, traineeUsername);
        Thread.sleep(1000);
    }

    @Then("an exception should be thrown when retrieving summary for trainee {string}")
    public void check_trainer_summary_for_deleted_trainee(String username) {
        assertThrows(RuntimeException.class, () -> {
            trainerWorkloadServiceFeignClient.getMonthlySummary(BEARER_PREFIX + accessToken, username);
        });
    }

    @Given("a trainee with username {string} and wrong password {string}")
    public void an_existing_trainee_with_invalid_credentials(String username, String wrongPassword) {
        loginRequestDto = new LoginRequestDto(username, wrongPassword);
    }

    @When("the trainee attempts to authenticate with invalid credentials")
    public void authenticate_with_invalid_credentials() {
        try {
            crmSystemFeignClient.authenticate(loginRequestDto);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("an exception should be thrown")
    public void check_trainer_summary_for_deleted_trainee() {
        assertNotNull(exception);
        exception = null;
    }

    @When("the authenticated trainee with username {string} attempts to create a training for nonexistent trainee")
    public void create_training_for_nonexistent_trainee(String authUserUsername) {
        accessToken = getAccessToken(authUserUsername);
        try {
            crmSystemFeignClient.createTraining(BEARER_PREFIX + accessToken, trainingDto);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the authenticated trainee with username {string} attempts to delete nonexistent trainee with username {string}")
    public void delete_non_existent_trainee(String authUserUsername, String username) {
        accessToken = getAccessToken(authUserUsername);
        try {
            crmSystemFeignClient.deleteTrainee(BEARER_PREFIX + accessToken, username);
        } catch (Exception e) {
            exception = e;
        }
    }

    private String getAccessToken(String username) {
        String password = USERS_PASSWORDS.get(username);
        LoginRequestDto loginRequestDto1 = new LoginRequestDto(username, password);
        return crmSystemFeignClient.authenticate(loginRequestDto1).getAccessToken();
    }
}
