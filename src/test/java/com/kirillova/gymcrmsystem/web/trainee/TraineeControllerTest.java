package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.TraineeTestData;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import com.kirillova.gymcrmsystem.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_MATCHER_WITH_TRAINER_LIST;
import static com.kirillova.gymcrmsystem.TraineeTestData.getUpdatedTraineeTo;
import static com.kirillova.gymcrmsystem.TrainerTestData.FREE_TRAINERS_FOR_TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_MATCHER;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_LIST_FOR_TRAINEE_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_TO_MATCHER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerTest extends AbstractSpringTest {
    private static final String REST_URL = TraineeController.REST_URL + '/';

    @Autowired
    private TraineeService traineeService;

    @Test
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
        UserTo userTo = UserTo.builder().username(USER_1_USERNAME).password(USER_1.getPassword()).newPassword(newPassword).build();
        perform(MockMvcRequestBuilders.put(REST_URL + USER_1.getUsername() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(status().isOk());

        clearSession();

        Assertions.assertEquals(newPassword, traineeService.get(USER_1_USERNAME).getUser().getPassword());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_1_USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_TO_MATCHER_WITH_TRAINER_LIST.contentJson(TRAINEE_TO_1));
    }

    @Test
    void update() throws Exception {
        TraineeTo traineeTo = getUpdatedTraineeTo();

        TraineeTo traineeToWithTrainerList = getUpdatedTraineeTo();
        traineeToWithTrainerList.setTrainerList(TRAINEE_TO_1.getTrainerList());

        perform(MockMvcRequestBuilders.put(REST_URL + USER_1_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(traineeTo)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINEE_TO_MATCHER_WITH_TRAINER_LIST.contentJson(traineeToWithTrainerList));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_1_USERNAME))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertNull(traineeService.get(USER_1.getUsername()));
    }

    @Test
    void getFreeTrainersForTrainee() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_1_USERNAME + "/free_trainers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_TO_MATCHER.contentJson(FREE_TRAINERS_FOR_TRAINEE_1));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_1_USERNAME + "/trainings"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TO_MATCHER.contentJson(TRAINING_TO_LIST_FOR_TRAINEE_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER_1_USERNAME)
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(traineeService.getWithUser(USER_1_USERNAME).getUser().isActive());
    }

}
