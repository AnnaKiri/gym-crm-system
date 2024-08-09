package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.BaseTest;
import com.kirillova.gymcrmsystem.TraineeTestData;
import com.kirillova.gymcrmsystem.dto.TraineeDto;
import com.kirillova.gymcrmsystem.dto.UserDto;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_DTO_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTraineeDto;
import static com.kirillova.gymcrmsystem.TrainerTestData.FREE_TRAINERS_FOR_TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_DTO_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_DTO_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_DTO_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static com.kirillova.gymcrmsystem.UserTestData.USER_6;
import static com.kirillova.gymcrmsystem.UserTestData.USER_DTO_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.jsonWithPassword;
import static com.kirillova.gymcrmsystem.config.SecurityConfig.PASSWORD_ENCODER;
import static com.kirillova.gymcrmsystem.web.trainee.TraineeController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerTest extends BaseTest {
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
        UserDto userDto = UserDto.builder().username(USER_1_USERNAME).password(USER_1.getPassword()).build();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1.getUsername() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword))
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertTrue(PASSWORD_ENCODER.matches(newPassword, traineeService.get(USER_1_USERNAME).getUser().getPassword()));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(TRAINEE_DTO_1));
    }

    @Test
    void update() throws Exception {
        TraineeDto traineeExpected = getUpdatedTraineeDto();
        traineeExpected.setTrainerList(TRAINEE_DTO_1.getTrainerList());

        TraineeDto traineeDto = TraineeDto.builder()
                .firstName(traineeExpected.getFirstName())
                .lastName(traineeExpected.getLastName())
                .birthday(traineeExpected.getBirthday())
                .address(traineeExpected.getAddress())
                .isActive(traineeExpected.getIsActive())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto))
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeExpected));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_1_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andDo(print())
                .andExpect(status().isOk());

        assertThrows(NotFoundException.class, () -> traineeService.get(USER_1.getUsername()));
    }

    @Test
    void getFreeTrainersForTrainee() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/free-trainers")
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(FREE_TRAINERS_FOR_TRAINEE_1));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/trainings")
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINEE_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .param("isActive", "false")
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andDo(print())
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertFalse(traineeService.getWithUser(USER_1_USERNAME).getUser().isActive());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "Not.Found")
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalid() throws Exception {
        TraineeDto traineeDto = TraineeDto.builder().build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto))
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateTrainerList() throws Exception {
        List<String> trainerUsernames = List.of(USER_5.getUsername(), USER_6.getUsername());

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME + "/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerUsernames))
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(List.of(TRAINER_DTO_1, TRAINER_DTO_2)));
    }

    @Test
    void setActiveAgain() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .param("isActive", "true")
                .header(HttpHeaders.AUTHORIZATION, BEARER_WITH_SPACE + tokens.get(USER_1_USERNAME)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
