package com.annakirillova.crmsystem.integration.trainee;

import com.annakirillova.common.dto.TraineeDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TraineeTestData;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.integration.BaseControllerIntegrationTest;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_1_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.getUpdatedTraineeDto;
import static com.annakirillova.crmsystem.TrainerTestData.FREE_TRAINERS_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_3;
import static com.annakirillova.crmsystem.UserTestData.USER_4;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_6;
import static com.annakirillova.crmsystem.UserTestData.USER_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.trainee.TraineeController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private TraineeService traineeService;

    @Test
    @DirtiesContext
    void register() throws Exception {
        TraineeDto newTraineeDto = TraineeTestData.getNewTraineeDto();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTraineeDto)))
                .andExpect(status().isCreated());

        UserDto created = USER_DTO_MATCHER.readFromJson(action);
        String expectedUsername = newTraineeDto.getFirstName() + "." + newTraineeDto.getLastName();
        Assertions.assertEquals(expectedUsername, created.getUsername());
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserDto userDto = UserDto.builder().username(USER_3.getUsername()).password(newPassword).build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_3.getUsername() + "/password")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_3).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andExpect(status().isOk());

        entityManager.clear();
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(TRAINEE_DTO_1_WITH_TRAINER_LIST));
    }

    @Test
    void update() throws Exception {
        TraineeDto traineeExpected = getUpdatedTraineeDto();
        traineeExpected.setTrainerList(TRAINEE_DTO_1_WITH_TRAINER_LIST.getTrainerList());

        TraineeDto traineeDto = TraineeDto.builder()
                .firstName(traineeExpected.getFirstName())
                .lastName(traineeExpected.getLastName())
                .birthday(traineeExpected.getBirthday())
                .address(traineeExpected.getAddress())
                .isActive(traineeExpected.getIsActive())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeExpected));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_4.getUsername())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_4).getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());

        assertThrows(NotFoundException.class, () -> traineeService.get(USER_4.getUsername()));
    }

    @Test
    void getFreeTrainersForTrainee() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/free-trainers")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(FREE_TRAINERS_FOR_TRAINEE_1));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/trainings")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINEE_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken())
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertFalse(traineeService.getWithUser(USER_1_USERNAME).getUser().isActive());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "Not.Found")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalid() throws Exception {
        TraineeDto traineeDto = TraineeDto.builder().build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateTrainerList() throws Exception {
        List<String> trainerUsernames = List.of(USER_5.getUsername(), USER_6.getUsername());

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME + "/trainers")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(List.of(TRAINER_DTO_1, TRAINER_DTO_2)));
    }

    @Test
    void setActiveAgain() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_1).getAccessToken())
                .param("isActive", "true"))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
