package com.annakirillova.crmsystem.web.training;

import com.annakirillova.crmsystem.BaseTest;
import com.annakirillova.crmsystem.TrainingTestData;
import com.annakirillova.crmsystem.dto.TrainingDto;
import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.jsonWithTypeId;
import static com.annakirillova.crmsystem.web.training.TrainingController.REST_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void create() throws Exception {
        doNothing().when(jmsTemplate).convertAndSend(any(String.class), any(TrainingInfoDto.class));

        TrainingDto newTrainingDto = TrainingTestData.getNewTrainingDto();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithTypeId(newTrainingDto, newTrainingDto.getTypeId())))
                .andExpect(status().isOk());

        verify(jmsTemplate, times(1)).convertAndSend(any(String.class), any(TrainingInfoDto.class));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINING_1_ID)
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + 9999)
                .with(jwt()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
