package com.annakirillova.crmsystem.web.trainee;

import com.annakirillova.crmsystem.TraineeTestData;
import com.annakirillova.crmsystem.dto.TraineeDto;
import com.annakirillova.crmsystem.dto.UserDto;
import com.annakirillova.crmsystem.metrics.RegisterMetrics;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.service.AuthService;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.service.TrainerService;
import com.annakirillova.crmsystem.util.JsonUtil;
import com.annakirillova.crmsystem.web.BaseControllerTest;
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

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_1_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.getUpdatedTrainee;
import static com.annakirillova.crmsystem.TraineeTestData.getUpdatedTraineeDto;
import static com.annakirillova.crmsystem.TrainerTestData.FREE_TRAINERS_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINERS_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_3;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_2;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_6;
import static com.annakirillova.crmsystem.UserTestData.getUpdatedUser;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.web.trainee.TraineeController.REST_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TraineeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TraineeControllerTest extends BaseControllerTest {
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
        TraineeDto newTraineeDto = TraineeTestData.getNewTraineeDto();
        Trainee createdTrainee = TraineeTestData.getNewTrainee();

        doNothing().when(registerMetrics).incrementRequestCount();
        doNothing().when(registerMetrics).recordExecutionTimeTrainee(any(Long.class), any(TimeUnit.class));
        when(traineeService.create(
                any(String.class),
                any(String.class),
                any(LocalDate.class),
                any(String.class),
                any(String.class)))
                .thenReturn(createdTrainee);

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTraineeDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(traineeService, times(1)).create(
                any(String.class),
                any(String.class),
                any(LocalDate.class),
                any(String.class),
                any(String.class)
        );
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserDto userDto = UserDto.builder().username(USER_1_USERNAME).password(newPassword).build();

        when(authService.getUsername()).thenReturn(USER_1_USERNAME);
        doNothing().when(traineeService).changePassword(
                any(String.class),
                any(String.class)
        );

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(traineeService, times(1)).changePassword(USER_1_USERNAME, newPassword);
    }

    @Test
    void get() throws Exception {
        when(traineeService.getWithUser(USER_1_USERNAME)).thenReturn(TRAINEE_1);
        when(trainerService.getTrainersForTrainee(USER_1_USERNAME)).thenReturn(TRAINERS_FOR_TRAINEE_1);

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(TRAINEE_DTO_1_WITH_TRAINER_LIST));

        verify(traineeService, times(1)).getWithUser(USER_1_USERNAME);
        verify(trainerService, times(1)).getTrainersForTrainee(USER_1_USERNAME);
    }

    @Test
    void update() throws Exception {
        TraineeDto traineeDtoExpected = getUpdatedTraineeDto();
        traineeDtoExpected.setTrainerList(TRAINEE_DTO_1_WITH_TRAINER_LIST.getTrainerList());

        doNothing().when(traineeService).update(
                any(String.class),
                any(String.class),
                any(String.class),
                any(LocalDate.class),
                any(String.class),
                any(Boolean.class)
        );

        Trainee traineeExpected = getUpdatedTrainee();
        User userExpected = getUpdatedUser();
        userExpected.setUsername(USER_1_USERNAME);
        traineeExpected.setUser(userExpected);

        when(traineeService.getWithUser(USER_1_USERNAME)).thenReturn(traineeExpected);
        when(trainerService.getTrainersForTrainee(USER_1_USERNAME)).thenReturn(TRAINERS_FOR_TRAINEE_1);

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDtoExpected)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeDtoExpected));

        verify(traineeService, times(1)).update(
                any(String.class),
                any(String.class),
                any(String.class),
                any(LocalDate.class),
                any(String.class),
                any(Boolean.class)
        );
    }

    @Test
    void delete() throws Exception {
        doNothing().when(traineeService).delete(any(String.class));

        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_1_USERNAME))
                .andDo(print())
                .andExpect(status().isOk());

        verify(traineeService, times(1)).delete(USER_1_USERNAME);
    }

    @Test
    void getFreeTrainersForTrainee() throws Exception {
        when(traineeService.getFreeTrainersForTrainee(USER_1_USERNAME)).thenReturn(new ArrayList<>(List.of(TRAINER_1, TRAINER_3)));

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/free-trainers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(FREE_TRAINERS_FOR_TRAINEE_1));

        verify(traineeService, times(1)).getFreeTrainersForTrainee(USER_1_USERNAME);
    }

    @Test
    void getTrainings() throws Exception {
        when(traineeService.getTrainings(
                any(String.class),
                nullable(LocalDate.class),
                nullable(LocalDate.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)))
                .thenReturn(new ArrayList<>(List.of(TRAINING_1, TRAINING_2)));

        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/trainings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINEE_1));

        verify(traineeService, times(1)).getTrainings(
                any(String.class),
                nullable(LocalDate.class),
                nullable(LocalDate.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class)
        );
    }

    @Test
    void setActive() throws Exception {
        when(traineeService.setActive(
                eq(USER_1_USERNAME),
                any(Boolean.class)))
                .thenReturn(true);

        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(traineeService, times(1)).setActive(eq(USER_1_USERNAME), any(Boolean.class));
    }

    @Test
    void updateTrainerList() throws Exception {
        List<String> trainerUsernames = List.of(USER_5.getUsername(), USER_6.getUsername());
        List<Trainer> trainerList = List.of(TRAINER_1, TRAINER_3);

        doNothing().when(traineeService).updateTrainerList(USER_1_USERNAME, trainerUsernames);
        when(trainerService.getTrainersForTrainee(USER_1_USERNAME)).thenReturn(trainerList);

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME + "/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerUsernames)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(traineeService, times(1)).updateTrainerList(USER_1_USERNAME, trainerUsernames);
        verify(trainerService, times(1)).getTrainersForTrainee(USER_1_USERNAME);
    }
}
