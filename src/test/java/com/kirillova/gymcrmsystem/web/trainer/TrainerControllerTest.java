package com.kirillova.gymcrmsystem.web.trainer;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.TrainerTestData;
import com.kirillova.gymcrmsystem.service.TrainerService;
import com.kirillova.gymcrmsystem.to.TrainerTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import com.kirillova.gymcrmsystem.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_MATCHER_WITH_TRAINEE_LIST;
import static com.kirillova.gymcrmsystem.TrainerTestData.getUpdatedTrainerTo;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_LIST_FOR_TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainingTestData.TRAINING_TO_MATCHER;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_TO_MATCHER;
import static com.kirillova.gymcrmsystem.web.trainer.TrainerController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerControllerTest extends AbstractSpringTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private TrainerService trainerService;

    @Test
    @DirtiesContext
    void register() throws Exception {
        TrainerTo newTrainerTo = TrainerTestData.getNewTrainerTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTrainerTo)))
                .andExpect(status().isCreated());

        UserTo created = USER_TO_MATCHER.readFromJson(action);
        String expectedUsername = newTrainerTo.getFirstName() + "." + newTrainerTo.getLastName();
        Assertions.assertEquals(expectedUsername, created.getUsername());
        Assertions.assertEquals(USER_1.getId() + 9, created.getId());
    }

    @Test
    void changePassword() throws Exception {
        String newPassword = "1234567890";
        UserTo userTo = UserTo.builder().username(USER_5_USERNAME).password(USER_5.getPassword()).newPassword(newPassword).build();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5.getUsername() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(status().isOk());

        clearSession();

        Assertions.assertEquals(newPassword, trainerService.get(USER_5_USERNAME).getUser().getPassword());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_TO_MATCHER_WITH_TRAINEE_LIST.contentJson(TRAINER_TO_1));
    }

    @Test
    void update() throws Exception {
        TrainerTo trainerTo = getUpdatedTrainerTo();

        TrainerTo trainerToWithTrainerList = getUpdatedTrainerTo();
        trainerToWithTrainerList.setTraineeList(TRAINER_TO_1.getTraineeList());

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_5_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(trainerTo)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINER_TO_MATCHER_WITH_TRAINEE_LIST.contentJson(trainerToWithTrainerList));
    }

    @Test
    void getTrainings() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_5_USERNAME + "/trainings"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRAINING_TO_MATCHER.contentJson(TRAINING_TO_LIST_FOR_TRAINER_1));
    }

    @Test
    void setActive() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + USER_5_USERNAME)
                .param("isActive", "false"))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertFalse(trainerService.getWithUserAndSpecialization(USER_5_USERNAME).getUser().isActive());
    }

}
