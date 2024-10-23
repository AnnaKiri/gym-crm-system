package com.annakirillova.trainerworkloadservice.web.trainer;

import com.annakirillova.trainerworkloadservice.config.AppConfig;
import com.annakirillova.trainerworkloadservice.exception.NotFoundException;
import com.annakirillova.trainerworkloadservice.service.TrainerSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AppConfig.class)
class TrainerControllerUnitTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerSummaryService trainerSummaryService;

    @Test
    void get() throws Exception {
        when(trainerSummaryService.get(TRAINER_SUMMARY_2.getUsername())).thenReturn(TRAINER_SUMMARY_2);

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINER_SUMMARY_2.getUsername() + "/monthly-summary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_MATCHER_WITH_SUMMARY_LIST.contentJson(TRAINER_SUMMARY_2));
    }

    @Test
    void getNotFound() throws Exception {
        when(trainerSummaryService.get("NoName")).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "NoName" + "/monthly-summary"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}