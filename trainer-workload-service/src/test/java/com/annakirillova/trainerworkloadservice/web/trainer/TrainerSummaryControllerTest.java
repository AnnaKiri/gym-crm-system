package com.annakirillova.trainerworkloadservice.web.trainer;

import com.annakirillova.trainerworkloadservice.BaseTest;
import com.annakirillova.trainerworkloadservice.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerSummaryControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        when(trainerRepository.getTrainerIfExists(TRAINER_SUMMARY_2.getUsername())).thenReturn(TRAINER_SUMMARY_2);

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINER_SUMMARY_2.getUsername() + "/monthly-summary")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_MATCHER_WITH_SUMMARY_LIST.contentJson(TRAINER_SUMMARY_2));
    }

    @Test
    void getNotFound() throws Exception {
        when(trainerRepository.getTrainerIfExists("NoName")).thenThrow(NotFoundException.class);

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "NoName" + "/monthly-summary")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }
}