package com.annakirillova.crmsystem.web.trainer;

import com.annakirillova.common.dto.TrainerDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TrainerTestData;
import com.annakirillova.crmsystem.metrics.RegisterMetrics;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.service.AuthService;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.web.BaseControllerUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEES_FOR_TRAINER_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1_WITH_TRAINEE_LIST;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST;
import static com.annakirillova.crmsystem.TrainerTestData.getUpdatedTrainer;
import static com.annakirillova.crmsystem.TrainerTestData.getUpdatedTrainerDto;
import static com.annakirillova.crmsystem.TrainerTestData.jsonWithSpecializationId;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_7;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINER_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_5_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.getUpdatedUser;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.web.trainer.TrainerController.REST_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrainerControllerUnitTest extends BaseControllerUnitTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private RegisterMetrics registerMetrics;

    @MockBean
    private AuthService authService;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TraineeService traineeService;

    @Test
    void register() throws Exception {
        TrainerDto newTrainerDto = TrainerTestData.getNewTrainerDto();
        Trainer createdTrainer = TrainerTestData.getNewTrainer();

        doNothing().when(registerMetrics).incrementRequestCount();
        doNothing().when(registerMetrics).recordExecutionTimeTrainee(any(Long.class), any(TimeUnit.class));
        when(trainerService.create(
                any(String.class),
                any(String.class),
                any(Integer.class),
                any(String.class)))
                .thenReturn(createdTrainer);

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(newTrainerDto, newTrainerDto.getSpecializationId())))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(trainerService, times(1)).create(
                any(String.class),
                any(String.class),
                any(Integer.class),
                any(String.class)
        );
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserDto userDto = UserDto.builder().username(USER_5_USERNAME).password(newPassword).build();

        when(authService.getUsername()).thenReturn(USER_5_USERNAME);
        doNothing().when(trainerService).changePassword(
                any(String.class),
                any(String.class)
        );

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5.getUsername() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(trainerService, times(1)).changePassword(USER_5_USERNAME, newPassword);
    }

    @Test
    void get() throws Exception {
        when(trainerService.getWithUserAndSpecialization(USER_5_USERNAME)).thenReturn(TRAINER_1);
        when(traineeService.getTraineesForTrainer(USER_5_USERNAME)).thenReturn(TRAINEES_FOR_TRAINER_1);

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(TRAINER_DTO_1_WITH_TRAINEE_LIST));

        verify(trainerService, times(1)).getWithUserAndSpecialization(USER_5_USERNAME);
        verify(traineeService, times(1)).getTraineesForTrainer(USER_5_USERNAME);
    }

    @Test
    void update() throws Exception {
        TrainerDto trainerDtoExpected = getUpdatedTrainerDto();
        trainerDtoExpected.setTraineeList(TRAINER_DTO_1_WITH_TRAINEE_LIST.getTraineeList());

        doNothing().when(trainerService).update(
                any(String.class),
                any(String.class),
                any(String.class),
                any(Integer.class),
                any(Boolean.class)
        );

        Trainer trainerExpected = getUpdatedTrainer();
        User userExpected = getUpdatedUser();
        userExpected.setUsername(USER_5_USERNAME);
        trainerExpected.setUser(userExpected);

        when(trainerService.getWithUserAndSpecialization(USER_5_USERNAME)).thenReturn(trainerExpected);
        when(traineeService.getTraineesForTrainer(USER_5_USERNAME)).thenReturn(TRAINEES_FOR_TRAINER_1);

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithSpecializationId(trainerDtoExpected, trainerDtoExpected.getSpecializationId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST.contentJson(trainerDtoExpected));

        verify(trainerService, times(1)).update(
                any(String.class),
                any(String.class),
                any(String.class),
                any(Integer.class),
                any(Boolean.class)
        );
    }

    @Test
    void getTrainings() throws Exception {
        when(trainerService.getTrainings(
                any(String.class),
                nullable(LocalDate.class),
                nullable(LocalDate.class),
                nullable(String.class),
                nullable(String.class)))
                .thenReturn(new ArrayList<>(List.of(TRAINING_7)));

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME + "/trainings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINER_1));

        verify(trainerService, times(1)).getTrainings(
                any(String.class),
                nullable(LocalDate.class),
                nullable(LocalDate.class),
                nullable(String.class),
                nullable(String.class)
        );
    }

    @Test
    void setActive() throws Exception {
        when(trainerService.setActive(
                eq(USER_5_USERNAME),
                any(Boolean.class)))
                .thenReturn(true);

        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_5_USERNAME)
                .with(jwt())
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(trainerService, times(1)).setActive(eq(USER_5_USERNAME), any(Boolean.class));
    }
}
