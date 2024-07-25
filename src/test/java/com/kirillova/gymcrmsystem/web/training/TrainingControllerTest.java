package com.kirillova.gymcrmsystem.web.training;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.TrainingTestData;
import com.kirillova.gymcrmsystem.to.TrainingTo;
import com.kirillova.gymcrmsystem.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingControllerTest extends AbstractSpringTest {
    private static final String REST_URL = TrainingController.REST_URL + '/';

    @Test
    void create() throws Exception {
        TrainingTo newTrainingTo = TrainingTestData.getNewTrainingTo();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTrainingTo)))
                .andExpect(status().isOk());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + TRAINING_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TO_MATCHER.contentJson(TRAINING_TO_1));
    }

}
