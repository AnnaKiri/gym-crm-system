package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.feign.TrainerWorkloadServiceFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceFeignClientHelper {

    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "updateTrainingInfoFallbackMethod")
    public void updateTrainingInfo(String token, TrainingInfoDto trainingInfoDto) {
        trainerWorkloadServiceFeignClient.updateTrainingInfo(token, trainingInfoDto);
    }

    public void updateTrainingInfoFallbackMethod(String token, TrainingInfoDto trainingInfoDto, Throwable throwable) {
        log.error("TrainerWorkloadService failed: {}", throwable.getMessage(), throwable);
    }
}

