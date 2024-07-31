package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.BaseTest;
import com.kirillova.gymcrmsystem.TraineeTestData;
import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import com.kirillova.gymcrmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_MATCHER_WITH_TRAINER_LIST;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTraineeTo;
import static com.kirillova.gymcrmsystem.TrainerTestData.FREE_TRAINERS_FOR_TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_LIST_FOR_TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_TO_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.jsonWithPassword;
import static com.kirillova.gymcrmsystem.web.trainee.TraineeController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerTest extends BaseTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private TraineeService traineeService;

    @Test
    @DirtiesContext
    void register() throws Exception {
        TraineeTo newTraineeTo = TraineeTestData.getNewTraineeTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTraineeTo)))
                .andExpect(status().isCreated());

        UserTo created = USER_TO_MATCHER.readFromJson(action);
        String expectedUsername = newTraineeTo.getFirstName() + "." + newTraineeTo.getLastName();
        Assertions.assertEquals(expectedUsername, created.getUsername());
        Assertions.assertEquals(USER_1.getId() + 9, created.getId());
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserTo userTo = UserTo.builder().username(USER_1_USERNAME).password(USER_1.getPassword()).build();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1.getUsername() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(userTo, newPassword)))
                .andExpect(status().isOk());

        clearSession();

        Assertions.assertEquals(newPassword, traineeService.get(USER_1_USERNAME).getUser().getPassword());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_TO_MATCHER_WITH_TRAINER_LIST.contentJson(TRAINEE_TO_1));
    }

    @Test
    void update() throws Exception {
        TraineeTo traineeExpected = getUpdatedTraineeTo();
        traineeExpected.setTrainerList(TRAINEE_TO_1.getTrainerList());

        TraineeTo traineeTo = TraineeTo.builder()
                .firstName(traineeExpected.getFirstName())
                .lastName(traineeExpected.getLastName())
                .birthday(traineeExpected.getBirthday())
                .address(traineeExpected.getAddress())
                .isActive(traineeExpected.getIsActive())
                .build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeTo)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_TO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeExpected));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_1_USERNAME))
                .andDo(print())
                .andExpect(status().isOk());

        assertThrows(NotFoundException.class, () -> traineeService.get(USER_1.getUsername()));
    }

    @Test
    void getFreeTrainersForTrainee() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/free-trainers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_TO_MATCHER.contentJson(FREE_TRAINERS_FOR_TRAINEE_1));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_1_USERNAME + "/trainings"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TO_MATCHER.contentJson(TRAINING_TO_LIST_FOR_TRAINEE_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertFalse(traineeService.getWithUser(USER_1_USERNAME).getUser().isActive());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "Not.Found"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalid() throws Exception {
        TraineeTo traineeTo = TraineeTo.builder().build();

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeTo)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateTrainerList() throws Exception {
        List<String> trainerUsernames = List.of(USER_5_USERNAME);

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_1_USERNAME + "/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_TO_MATCHER.contentJson(List.of(TRAINER_TO_1, TRAINER_TO_2, TRAINER_TO_4)));
    }

    @Test
    void setActiveAgain() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_1_USERNAME)
                .param("isActive", "true"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
