package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.feign.TrainerWorkloadServiceFeignClient;
import com.annakirillova.crmsystem.util.FeignExceptionUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceFeignClientHelper {

    private static final String SERVICE_NAME = "Trainer Workload Service";

    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "updateTrainingInfoFallbackMethod")
    public void updateTrainingInfo(String token, TrainingInfoDto trainingInfoDto) {
        trainerWorkloadServiceFeignClient.updateTrainingInfo(token, trainingInfoDto);
    }

    private void updateTrainingInfoFallbackMethod(String token, TrainingInfoDto trainingInfoDto, Throwable throwable) throws Throwable {
        Map<String, String> exceptionMessages = FeignExceptionUtil.getExceptionMessages(SERVICE_NAME, throwable);
        throw FeignExceptionUtil.handleFeignException(throwable, exceptionMessages);
    }
}

