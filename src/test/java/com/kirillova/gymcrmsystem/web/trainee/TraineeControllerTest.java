package com.kirillova.gymcrmsystem.web.trainee;

import com.kirillova.gymcrmsystem.AbstractSpringTest;
import com.kirillova.gymcrmsystem.TraineeTestData;
import com.kirillova.gymcrmsystem.service.TraineeService;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import com.kirillova.gymcrmsystem.to.UserTo;
import com.kirillova.gymcrmsystem.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_TO_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerTest extends AbstractSpringTest {
    private static final String REST_URL = TraineeController.REST_URL + '/';

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

}
