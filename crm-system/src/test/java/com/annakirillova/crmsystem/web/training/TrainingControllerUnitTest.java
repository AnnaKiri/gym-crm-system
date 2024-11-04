package com.annakirillova.crmsystem.web.training;

import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.crmsystem.TrainingTestData;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.service.TrainingService;
import com.annakirillova.crmsystem.util.JsonUtil;
import com.annakirillova.crmsystem.web.BaseControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_3;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.getNewTraining;
import static com.annakirillova.crmsystem.UserTestData.USER_3;
import static com.annakirillova.crmsystem.UserTestData.USER_7;
import static com.annakirillova.crmsystem.web.training.TrainingController.REST_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrainingControllerUnitTest extends BaseControllerUnitTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private TrainingService trainingService;

    @MockBean
    private TraineeService traineeService;

    @MockBean
    private TrainerService trainerService;

    @Test
    void create() throws Exception {
        when(traineeService.get(USER_3.getUsername())).thenReturn(TRAINEE_1);
        when(trainerService.get(USER_7.getUsername())).thenReturn(TRAINER_3);
        when(trainingService.create(any(Trainee.class),
                any(Trainer.class),
                any(String.class),
                any(Integer.class),
                any(LocalDate.class),
                anyInt())).
                thenReturn(getNewTraining());

        TrainingDto newTrainingDto = TrainingTestData.getNewTrainingDto();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTrainingDto)))
                .andExpect(status().isOk());

        verify(traineeService, times(1)).get(any(String.class));
        verify(trainerService, times(1)).get(any(String.class));
        verify(trainingService, times(1)).create(any(Trainee.class),
                any(Trainer.class),
                any(String.class),
                any(Integer.class),
                any(LocalDate.class),
                anyInt());
    }

    @Test
    void get() throws Exception {
        when(trainingService.getFull(TRAINING_1_ID)).thenReturn(TRAINING_1);

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRAINING_1_ID)
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_1));

        verify(trainingService, times(1)).getFull(anyInt());
    }

    @Test
    void getNotFound() throws Exception {
        when(trainingService.getFull(9999)).thenThrow(NotFoundException.class);

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + 9999)
                .with(jwt()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
