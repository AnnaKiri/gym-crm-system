package com.annakirillova.crmsystem.component.trainee;

import com.annakirillova.common.dto.CredentialRepresentationDto;
import com.annakirillova.common.dto.TraineeDto;
import com.annakirillova.common.dto.TrainingInfoDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.TraineeTestData;
import com.annakirillova.crmsystem.component.BaseControllerComponentTest;
import com.annakirillova.crmsystem.dto.KeycloakUserDto;
import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.service.KeycloakAuthFeignClientHelper;
import com.annakirillova.crmsystem.service.KeycloakFeignClientHelper;
import com.annakirillova.crmsystem.service.TraineeService;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static com.annakirillova.crmsystem.FeignClientTestData.KEYCLOAK_USER_RESPONSE;
import static com.annakirillova.crmsystem.FeignClientTestData.TOKEN_RESPONSE_DTO;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_1_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST;
import static com.annakirillova.crmsystem.TraineeTestData.getUpdatedTraineeDto;
import static com.annakirillova.crmsystem.TrainerTestData.FREE_TRAINERS_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_LIST_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_6;
import static com.annakirillova.crmsystem.UserTestData.USER_DTO_MATCHER;
import static com.annakirillova.crmsystem.UserTestData.jsonWithPassword;
import static com.annakirillova.crmsystem.web.trainee.TraineeController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerComponentTest extends BaseControllerComponentTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockBean
    private KeycloakAuthFeignClientHelper keycloakAuthFeignClientHelper;

    @MockBean
    private KeycloakFeignClientHelper keycloakFeignClientHelper;

    @Autowired
    private TraineeService traineeService;

    @Test
    @DirtiesContext
    void register() throws Exception {
        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);
        doNothing().when(keycloakFeignClientHelper).createUserWithCircuitBreaker(
                any(String.class), any(KeycloakUserDto.class)
        );

        TraineeDto newTraineeDto = TraineeTestData.getNewTraineeDto();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTraineeDto)))
                .andExpect(status().isCreated());

        UserDto created = USER_DTO_MATCHER.readFromJson(action);
        String expectedUsername = newTraineeDto.getFirstName() + "." + newTraineeDto.getLastName();
        Assertions.assertEquals(expectedUsername, created.getUsername());

        verify(keycloakAuthFeignClientHelper, times(1)).requestTokenWithCircuitBreaker(
                any(Map.class)
        );
        verify(keycloakFeignClientHelper, times(1)).createUserWithCircuitBreaker(
                any(String.class),
                any(KeycloakUserDto.class));
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserDto userDto = UserDto.builder().username(USER_1_USERNAME).password(newPassword).build();

        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);
        when(keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(any(String.class), any(String.class))).thenReturn(KEYCLOAK_USER_RESPONSE);
        doNothing().when(keycloakFeignClientHelper).updatePasswordWithCircuitBreaker(
                any(String.class), any(String.class), any(CredentialRepresentationDto.class)
        );

        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "HS256")
                .claim("preferred_username", USER_1_USERNAME)
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1.getUsername() + "/password")
                .with(jwt().jwt(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userDto, newPassword)))
                .andExpect(status().isOk());

        entityManager.clear();

        verify(keycloakAuthFeignClientHelper, times(2)).requestTokenWithCircuitBreaker(any(Map.class));
        verify(keycloakFeignClientHelper, times(1)).getUserByUsernameWithCircuitBreaker(any(String.class), any(String.class));
        verify(keycloakFeignClientHelper, times(1)).updatePasswordWithCircuitBreaker(any(String.class), any(String.class), any(CredentialRepresentationDto.class));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME)
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(TRAINEE_DTO_1_WITH_TRAINER_LIST));
    }

    @Test
    void update() throws Exception {
        TraineeDto traineeExpected = getUpdatedTraineeDto();
        traineeExpected.setTrainerList(TRAINEE_DTO_1_WITH_TRAINER_LIST.getTrainerList());

        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);
        when(keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(any(String.class), any(String.class))).thenReturn(KEYCLOAK_USER_RESPONSE);
        doNothing().when(keycloakFeignClientHelper).updateUserWithCircuitBreaker(
                any(String.class), any(String.class), any(KeycloakUserDto.class)
        );

        TraineeDto traineeDto = TraineeDto.builder()
                .firstName(traineeExpected.getFirstName())
                .lastName(traineeExpected.getLastName())
                .birthday(traineeExpected.getBirthday())
                .address(traineeExpected.getAddress())
                .isActive(traineeExpected.getIsActive())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeExpected));

        verify(keycloakAuthFeignClientHelper, times(2)).requestTokenWithCircuitBreaker(any(Map.class));
        verify(keycloakFeignClientHelper, times(1)).getUserByUsernameWithCircuitBreaker(any(String.class), any(String.class));
        verify(keycloakFeignClientHelper, times(1)).updateUserWithCircuitBreaker(any(String.class), any(String.class), any(KeycloakUserDto.class));
    }

    @Test
    void delete() throws Exception {

        when(keycloakAuthFeignClientHelper.requestTokenWithCircuitBreaker(any(Map.class))).thenReturn(TOKEN_RESPONSE_DTO);
        when(keycloakFeignClientHelper.getUserByUsernameWithCircuitBreaker(any(String.class), any(String.class))).thenReturn(KEYCLOAK_USER_RESPONSE);
        doNothing().when(keycloakFeignClientHelper).deleteUserWithCircuitBreaker(
                any(String.class), any(String.class)
        );
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(TrainingInfoDto.class));

        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_1_USERNAME)
                .with(jwt()))
                .andDo(print())
                .andExpect(status().isOk());

        assertThrows(NotFoundException.class, () -> traineeService.get(USER_1.getUsername()));

        verify(keycloakAuthFeignClientHelper, times(2)).requestTokenWithCircuitBreaker(any(Map.class));
        verify(keycloakFeignClientHelper, times(1)).getUserByUsernameWithCircuitBreaker(any(String.class), any(String.class));
        verify(keycloakFeignClientHelper, times(1)).deleteUserWithCircuitBreaker(any(String.class), any(String.class));
        verify(jmsTemplate, times(2)).convertAndSend(anyString(), any(TrainingInfoDto.class));
    }

    @Test
    void getFreeTrainersForTrainee() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/free-trainers")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(FREE_TRAINERS_FOR_TRAINEE_1));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/trainings")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_DTO_MATCHER.contentJson(TRAINING_DTO_LIST_FOR_TRAINEE_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .with(jwt())
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        entityManager.clear();

        Assertions.assertFalse(traineeService.getWithUser(USER_1_USERNAME).getUser().isActive());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "Not.Found")
                .with(jwt()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalid() throws Exception {
        TraineeDto traineeDto = TraineeDto.builder().build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateTrainerList() throws Exception {
        List<String> trainerUsernames = List.of(USER_5.getUsername(), USER_6.getUsername());

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME + "/trainers")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_DTO_MATCHER.contentJson(List.of(TRAINER_DTO_1, TRAINER_DTO_2)));
    }

    @Test
    void setActiveAgain() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .with(jwt())
                .param("isActive", "true"))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
