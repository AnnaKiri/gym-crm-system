package com.annakirillova.trainerworkloadservice.web.trainer;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.SummaryTestData.SUMMARY_DTO_LIST_FOR_TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_DTO_MATCHER_WITH_SUMMARY;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINER_2.getUsername() + "/monthly-summary")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_SUMMARY.contentJson(trainerDto));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "NoName" + "/monthly-summary")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }
}