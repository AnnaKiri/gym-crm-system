package com.kirillova.gymcrmsystem.web.training;

import com.kirillova.gymcrmsystem.BaseTest;
import com.kirillova.gymcrmsystem.TrainingTestData;
import com.kirillova.gymcrmsystem.dto.TrainingDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_DTO_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.jsonWithTypeId;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_3;
import static com.kirillova.gymcrmsystem.web.training.TrainingController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void create() throws Exception {
        TrainingDto newTrainingDto = TrainingTestData.getNewTrainingDto();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithTypeId(newTrainingDto, newTrainingDto.getTypeId()))
                .header("Authorization", "Bearer " + tokens.get(USER_3.getUsername())))
                .andExpect(status().isOk());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINING_1_ID)
                .header("Authorization", "Bearer " + tokens.get(USER_1_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + 9999)
                .header("Authorization", "Bearer " + tokens.get(USER_1_USERNAME)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
