package com.annakirillova.e2etests;

import com.annakirillova.common.dto.LoginRequestDto;
import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.common.dto.TraineeDto;
import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.common.dto.TrainingDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GymCrmSystemE2ETest extends BaseE2ETest {

    private static String accessToken;

    @Test
    @Order(1)
    void registerTrainee() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Doe");
        traineeDto.setBirthday(LocalDate.of(2000, 1, 1));
        traineeDto.setAddress("123 Main St");
        traineeDto.setIsActive(true);

        ResponseEntity<LoginRequestDto> registrationResponse = crmSystemFeignClient.register(traineeDto);
        assertEquals(201, registrationResponse.getStatusCodeValue());
    }

    @Test
    @Order(2)
    void authenticate() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("Angelina.Jolie", "password1");

        TokenResponseDto tokenResponse = crmSystemFeignClient.authenticate(loginRequestDto);
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getAccessToken());

        accessToken = tokenResponse.getAccessToken();
    }

    @Test
    @Order(3)
    void createTraining() throws InterruptedException {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setName("New Training");
        trainingDto.setTypeId(1);
        trainingDto.setDate(LocalDate.of(2024, 10, 5));
        trainingDto.setDuration(60);
        trainingDto.setTraineeUsername("John.Doe");
        trainingDto.setTrainerUsername("Sandra.Bullock");

        crmSystemFeignClient.createTraining(BEARER_PREFIX + accessToken, trainingDto);

        Thread.sleep(DELAY_MS);

        TrainerSummaryDto.Summary summary = new TrainerSummaryDto.Summary(2024, 10, 60);

        List<TrainerSummaryDto.Summary> summaryList = new ArrayList<>();
        summaryList.add(summary);

        TrainerSummaryDto expectedTrainerSummaryDto = new TrainerSummaryDto();
        expectedTrainerSummaryDto.setUsername("Sandra.Bullock");
        expectedTrainerSummaryDto.setFirstName("Sandra");
        expectedTrainerSummaryDto.setLastName("Bullock");
        expectedTrainerSummaryDto.setIsActive(true);
        expectedTrainerSummaryDto.setSummaryList(summaryList);

        TrainerSummaryDto actualSummaryDto = trainerWorkloadServiceFeignClient.getMonthlySummary(BEARER_PREFIX + accessToken, "Sandra.Bullock");
        assertNotNull(actualSummaryDto);
        assertEquals(expectedTrainerSummaryDto, actualSummaryDto);
    }

    @Test
    @Order(4)
    void deleteTrainee() throws InterruptedException {
        crmSystemFeignClient.deleteTrainee(BEARER_PREFIX + accessToken, "John.Doe");

        Thread.sleep(DELAY_MS);

        assertThrows(RuntimeException.class, () -> {
            trainerWorkloadServiceFeignClient.getMonthlySummary(BEARER_PREFIX + accessToken, "John.Doe");
        });
    }

    @Test
    @Order(5)
    void authenticateWithInvalidCredentials() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("John.Doe", "wrongPassword");

        assertThrows(RuntimeException.class, () -> {
            crmSystemFeignClient.authenticate(loginRequestDto);
        });
    }

    @Test
    @Order(6)
    void createTrainingForNonExistentTrainee() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setName("New Training");
        trainingDto.setTypeId(1);
        trainingDto.setDate(LocalDate.of(2024, 10, 5));
        trainingDto.setDuration(60);
        trainingDto.setTraineeUsername("John.Doe");
        trainingDto.setTrainerUsername("Not.Valid");

        assertThrows(RuntimeException.class, () -> {
            crmSystemFeignClient.createTraining(BEARER_PREFIX + accessToken, trainingDto);
        });
    }

    @Test
    @Order(7)
    void deleteNonExistentTrainee() {
        assertThrows(RuntimeException.class, () -> {
            crmSystemFeignClient.deleteTrainee(BEARER_PREFIX + accessToken, "NonExistentUser");
        });
    }
}
