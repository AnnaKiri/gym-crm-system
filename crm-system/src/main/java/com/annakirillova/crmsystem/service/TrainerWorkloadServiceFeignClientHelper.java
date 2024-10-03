package com.annakirillova.crmsystem.service;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import com.annakirillova.crmsystem.feign.TrainerWorkloadServiceFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceFeignClientHelper {

    private final TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;
    private final ExecutorService traceableExecutor;

    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "updateTrainingInfoFallbackMethod")
    @TimeLimiter(name = "trainerWorkloadService")
    public CompletableFuture<Void> updateTrainingInfo(String token, TrainingInfoDto trainingInfoDto) {
        return CompletableFuture.runAsync(() -> trainerWorkloadServiceFeignClient.updateTrainingInfo(token, trainingInfoDto), traceableExecutor);
    }

    public CompletableFuture<Void> updateTrainingInfoFallbackMethod(String token, TrainingInfoDto trainingInfoDto, Throwable throwable) {
        log.error("TrainerWorkloadService sent an error response: {}", throwable.getMessage(), throwable);
        return CompletableFuture.completedFuture(null);
    }
}
