package com.annakirillova.crmsystem.feign;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("trainer-workload-service")
public interface TrainerWorkloadServiceFeignClient {

    Logger log = LoggerFactory.getLogger(TrainerWorkloadServiceFeignClient.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/summaries")
    @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "updateTrainingInfoFallbackMethod")
    void updateTrainingInfo(@RequestHeader("Authorization") String token,
                            @RequestBody TrainingInfoDto trainingInfoDto);

    default void updateTrainingInfoFallbackMethod(Throwable throwable) {
        log.error("TrainerWorkloadService sent an error response: {}", throwable.getMessage(), throwable);
    }
}
