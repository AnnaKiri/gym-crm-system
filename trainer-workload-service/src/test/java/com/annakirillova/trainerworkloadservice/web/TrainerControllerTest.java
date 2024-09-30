package com.annakirillova.trainerworkloadservice.web;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import com.annakirillova.trainerworkloadservice.security.JWTProvider;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_DTO_LIST_FOR_TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_DTO_MATCHER_WITH_SUMMARY;
import static com.annakirillova.trainerworkloadservice.web.TrainerController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        TrainerDto trainerDto = TrainerDto.builder()
                .username(TRAINER_2.getUsername())
                .firstName(TRAINER_2.getFirstName())
                .lastName(TRAINER_2.getLastName())
                .isActive(TRAINER_2.isActive())
                .summaryList(SUMMARY_DTO_LIST_FOR_TRAINER_2)
                .build();

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINER_2.getUsername() + "/monthly_summary")
                .header(HttpHeaders.AUTHORIZATION, JWTProvider.BEARER_PREFIX + tokens.get("TEST")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_SUMMARY.contentJson(trainerDto));
    }
}