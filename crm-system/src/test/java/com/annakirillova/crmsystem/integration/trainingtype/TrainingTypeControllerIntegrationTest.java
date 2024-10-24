package com.annakirillova.crmsystem.integration.trainingtype;

import com.annakirillova.crmsystem.integration.BaseControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_LIST;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.config.SecurityConfig.BEARER_PREFIX;
import static com.annakirillova.crmsystem.web.trainingtype.TrainingTypeController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingTypeControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getTokensForUser(USER_5).getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TYPE_MATCHER.contentJson(TRAINING_TYPE_LIST));
    }
}

