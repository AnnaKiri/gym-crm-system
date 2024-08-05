package com.kirillova.gymcrmsystem.web.trainingtype;

import com.kirillova.gymcrmsystem.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_LIST;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;
import static com.kirillova.gymcrmsystem.web.trainingtype.TrainingTypeController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingTypeControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .header("Authorization", getAuthorizationHeader()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TYPE_MATCHER.contentJson(TRAINING_TYPE_LIST));
    }

}
