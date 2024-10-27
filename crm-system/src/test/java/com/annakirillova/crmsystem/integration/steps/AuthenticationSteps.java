package com.annakirillova.crmsystem.integration.steps;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.crmsystem.util.JsonUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.annakirillova.crmsystem.FeignClientTestData.LOGIN_REQUEST_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.AuthController.REST_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationSteps extends BaseSteps {

    private ResultActions resultActions;
    private TokenResponseDto tokenResponseDto;
    private LoginRequestDto loginRequestDto;

    @Given("a user with username {string} and password {string}")
    public void a_user_with_username_and_password(String username, String password) {
        loginRequestDto = new LoginRequestDto(username, password);
    }

    @When("the user attempts to log in")
    public void the_user_attempts_to_log_in() throws Exception {
        resultActions = perform(post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginRequestDto)))
                .andDo(print());
    }

    @Then("the authentication should succeed and return a token")
    public void the_authentication_should_succeed_and_return_a_token() throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        tokenResponseDto = TOKEN_RESPONSE_DTO_MATCHER.readFromJson(resultActions);
        assertNotNull(tokenResponseDto);
    }

    @Then("the authentication should fail with an unauthorized status with reason wrong credentials")
    public void the_authentication_should_fail_with_an_unauthorized_status_reason_credentials() throws Exception {
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Keycloak Open Id API Service, Wrong credentials")));
    }

    @Then("the authentication should fail with an unauthorized status with reason you're blocked")
    public void the_authentication_should_fail_with_an_unauthorized_status_reason_blocked() throws Exception {
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("User is blocked due to multiple failed login attempts. Please try again later.")));
    }

    @Given("a logged-in user with a valid token")
    public void a_logged_in_user_with_a_valid_token() throws Exception {
        tokenResponseDto = getTokensForUser(USER_1);
    }

    @When("the user attempts to log out")
    public void the_user_attempts_to_log_out() throws Exception {
        resultActions = perform(post(REST_URL + "/logout")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + tokenResponseDto.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(tokenResponseDto)))
                .andDo(print());
    }

    @Then("the logout should succeed with a success message")
    public void the_logout_should_succeed_with_a_success_message() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }

    @Given("a user with an invalid access token")
    public void a_user_with_an_access_invalid_token() throws Exception {
        tokenResponseDto = getTokensForUser(USER_1);
        tokenResponseDto.setAccessToken(TOKEN_RESPONSE_DTO.getAccessToken());
    }

    @Then("the logout should fail with an unauthorized status")
    public void the_logout_should_fail_with_an_unauthorized_status() throws Exception {
        resultActions.andExpect(status().isUnauthorized());
    }

    @Given("a user with wrong credentials")
    public void a_user_with_wrong_credentials() throws Exception {
        loginRequestDto = LOGIN_REQUEST_DTO;
    }

    @Given("a user with an invalid refresh token")
    public void a_user_with_an_invalid_token() throws Exception {
        tokenResponseDto = getTokensForUser(USER_1);
        tokenResponseDto.setRefreshToken(TOKEN_RESPONSE_DTO.getRefreshToken());
    }

    @Then("the logout should fail with a bad request status")
    public void the_logout_should_fail_with_an_bad_request_status() throws Exception {
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid refresh token")));
    }
}
