package com.kirillova.gymcrmsystem.feign;

import com.kirillova.gymcrmsystem.dto.TrainingInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("trainer-workload-service")
public interface TrainerWorkloadServiceFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/trainings")
    void updateTrainingInfo(@RequestBody TrainingInfoDto trainingInfoDto);
}
