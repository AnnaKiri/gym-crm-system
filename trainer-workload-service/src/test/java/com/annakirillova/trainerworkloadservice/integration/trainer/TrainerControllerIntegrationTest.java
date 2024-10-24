package com.annakirillova.trainerworkloadservice.integration.trainer;

import com.annakirillova.trainerworkloadservice.integration.BaseControllerIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_2_USERNAME;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerControllerIntegrationTest extends BaseControllerIntegrationTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
        mongoTemplate.save(TRAINER_SUMMARY_2);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINER_2_USERNAME + "/monthly-summary")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAccessToken(TRAINER_2_USERNAME, "password6")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_MATCHER_WITH_SUMMARY_LIST.contentJson(TRAINER_SUMMARY_2));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "NoName" + "/monthly-summary")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAccessToken(TRAINER_2_USERNAME, "password6")))
                .andExpect(status().isNotFound());
    }
}
