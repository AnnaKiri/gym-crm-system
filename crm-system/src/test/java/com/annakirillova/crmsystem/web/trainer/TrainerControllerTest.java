package com.annakirillova.crmsystem.web.trainer;

import com.annakirillova.crmsystem.BaseTest;
import com.annakirillova.crmsystem.TrainerTestData;
import com.annakirillova.crmsystem.dto.TrainerDto;
import com.annakirillova.crmsystem.dto.UserDto;
import com.annakirillova.crmsystem.security.JWTProvider;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST;
import static com.annakirillova.crmsystem.TrainerTestData.getUpdatedTrainerDto;
import static com.annakirillova.crmsystem.TrainerTestData.jsonWithSpecializationId;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINER_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_5_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.config.SecurityConfig.PASSWORD_ENCODER;
import static com.annakirillova.crmsystem.web.trainer.TrainerController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerControllerTest extends BaseTest {
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
        UserDto userDto = UserDto.builder().username(USER_5_USERNAME).password(USER_5.getPassword()).build();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5.getUsername() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword))
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertTrue(PASSWORD_ENCODER.matches(newPassword, trainerService.get(USER_5_USERNAME).getUser().getPassword()));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME)
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(TRAINER_DTO_1));
    }

    @Test
    void update() throws Exception {
        TrainerDto trainerExpected = getUpdatedTrainerDto();
        trainerExpected.setTraineeList(TRAINER_DTO_1.getTraineeList());

        TrainerDto trainerDto = TrainerDto.builder()
                .firstName(trainerExpected.getFirstName())
                .lastName(trainerExpected.getLastName())
                .specializationId(trainerExpected.getSpecializationId())
                .isActive(trainerExpected.getIsActive())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(trainerDto, trainerDto.getSpecializationId()))
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(trainerExpected));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME + "/trainings")
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINER_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_5_USERNAME)
                .param("isActive", "false")
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andDo(print())
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertFalse(trainerService.getWithUserAndSpecialization(USER_5_USERNAME).getUser().isActive());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "Not.Found")
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalid() throws Exception {
        TrainerDto trainerDto = TrainerDto.builder().build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerDto))
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setActiveAgain() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_5_USERNAME)
                .param("isActive", "true")
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get(USER_5_USERNAME)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
