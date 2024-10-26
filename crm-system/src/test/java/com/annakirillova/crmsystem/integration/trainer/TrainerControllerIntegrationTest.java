package com.annakirillova.crmsystem.integration.trainer;

import com.annakirillova.common.dto.TrainerDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TrainerTestData;
import com.annakirillova.crmsystem.integration.BaseControllerIntegrationTest;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1_WITH_TRAINEE_LIST;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST;
import static com.annakirillova.crmsystem.TrainerTestData.getUpdatedTrainerDto;
import static com.annakirillova.crmsystem.TrainerTestData.jsonWithSpecializationId;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINER_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_5_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_8;
import static com.annakirillova.crmsystem.UserTestData.USER_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.trainer.TrainerController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private TrainerService trainerService;

    @Test
    @DirtiesContext
    void register() throws Exception {
        TrainerDto newTrainerDto = TrainerTestData.getNewTrainerDto();

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(newTrainerDto, newTrainerDto.getSpecializationId())))
                .andExpect(status().isCreated());

        UserDto created = USER_DTO_MATCHER.readFromJson(action);
        String expectedUsername = newTrainerDto.getFirstName() + "." + newTrainerDto.getLastName();
        Assertions.assertEquals(expectedUsername, created.getUsername());
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserDto userDto = UserDto.builder().username(USER_8.getUsername()).password(newPassword).build();

        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "HS256")
                .claim("preferred_username", USER_8.getUsername())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_8.getUsername() + "/password")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_8).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andExpect(status().isOk());

        entityManager.clear();
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(TRAINER_DTO_1_WITH_TRAINEE_LIST));
    }

    @Test
    void update() throws Exception {
        TrainerDto trainerExpected = getUpdatedTrainerDto();
        trainerExpected.setTraineeList(TRAINER_DTO_1_WITH_TRAINEE_LIST.getTraineeList());

        TrainerDto trainerDto = TrainerDto.builder()
                .firstName(trainerExpected.getFirstName())
                .lastName(trainerExpected.getLastName())
                .specializationId(trainerExpected.getSpecializationId())
                .isActive(trainerExpected.getIsActive())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(trainerDto, trainerDto.getSpecializationId())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(trainerExpected));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME + "/trainings")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINER_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_5_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken())
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertFalse(trainerService.getWithUserAndSpecialization(USER_5_USERNAME).getUser().isActive());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "Not.Found")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalid() throws Exception {
        TrainerDto trainerDto = TrainerDto.builder().build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setActiveAgain() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_5_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken())
                .param("isActive", "true"))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
