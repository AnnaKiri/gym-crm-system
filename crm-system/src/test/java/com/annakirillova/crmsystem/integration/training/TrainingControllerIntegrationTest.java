package com.annakirillova.crmsystem.integration.training;

import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.crmsystem.TrainingTestData;
import com.annakirillova.crmsystem.integration.BaseControllerIntegrationTest;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_7;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.training.TrainingController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void create() throws Exception {
        TrainingDto newTrainingDto = TrainingTestData.getNewTrainingDto();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_7).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTrainingDto)))
                .andExpect(status().isOk());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINING_1_ID)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + 9999)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
