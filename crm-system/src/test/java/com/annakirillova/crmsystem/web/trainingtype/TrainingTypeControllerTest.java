package com.annakirillova.crmsystem.web.trainingtype;

import com.annakirillova.crmsystem.repository.TrainingTypeRepository;
import com.annakirillova.crmsystem.web.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_LIST;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_MATCHER;
import static com.annakirillova.crmsystem.web.trainingtype.TrainingTypeController.REST_URL;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrainingTypeControllerTest extends BaseControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void get() throws Exception {
        when(trainingTypeRepository.findAll()).thenReturn(TRAINING_TYPE_LIST);

        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TYPE_MATCHER.contentJson(TRAINING_TYPE_LIST));
    }

}
