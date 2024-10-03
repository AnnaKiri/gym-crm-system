package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.feign.TrainerWorkloadServiceFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceFeignClientHelper {

    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "updateTrainingInfoFallbackMethod")
    @TimeLimiter(name = "trainerWorkloadService")
    public CompletableFuture<Void> updateTrainingInfo(String token, TrainingInfoDto trainingInfoDto) {
        return CompletableFuture.runAsync(() -> trainerWorkloadServiceFeignClient.updateTrainingInfo(token, trainingInfoDto));
    }

    public CompletableFuture<Void> updateTrainingInfoFallbackMethod(String token, TrainingInfoDto trainingInfoDto, Throwable throwable) {
        log.error("TrainerWorkloadService sent an error response: {}", throwable.getMessage(), throwable);
        return CompletableFuture.completedFuture(null);
    }
}

