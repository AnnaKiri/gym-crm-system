package com.annakirillova.trainerworkloadservice.component;

import com.annakirillova.trainerworkloadservice.exception.NotFoundException;
import com.annakirillova.trainerworkloadservice.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_MATCHER_WITH_SUMMARY_LIST;
import static com.annakirillova.trainerworkloadservice.TestData.TRAINER_SUMMARY_2;
import static com.annakirillova.trainerworkloadservice.web.trainer.TrainerController.REST_URL;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, JmsAutoConfiguration.class})
@ActiveProfiles("component-test")
class TrainerControllerComponentTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private TrainerRepository trainerRepository;

    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

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
