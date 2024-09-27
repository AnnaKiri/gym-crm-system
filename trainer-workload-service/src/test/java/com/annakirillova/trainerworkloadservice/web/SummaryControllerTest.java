package com.annakirillova.trainerworkloadservice.web;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.error.IllegalRequestDataException;
import com.annakirillova.trainerworkloadservice.security.JWTProvider;
import com.annakirillova.trainerworkloadservice.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_DTO_ADD;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_DTO_DELETE;
import static com.annakirillova.trainerworkloadservice.TrainingTestData.TRAINING_DTO_INVALID_ACTION_TYPE;
import static com.annakirillova.trainerworkloadservice.web.SummaryController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SummaryControllerTest extends BaseTest {

    @Test
    void updateSummaryAddCase() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get("TEST"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TRAINING_DTO_ADD)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSummaryDeleteCase() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get("TEST"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TRAINING_DTO_DELETE)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSummaryInvalidActionType() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get("TEST"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(TRAINING_DTO_INVALID_ACTION_TYPE)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalRequestDataException))
                .andExpect(result -> assertEquals("Wrong action type", result.getResolvedException().getMessage()));
    }
}