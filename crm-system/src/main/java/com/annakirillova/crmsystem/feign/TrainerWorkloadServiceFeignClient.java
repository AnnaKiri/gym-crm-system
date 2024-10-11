package com.annakirillova.crmsystem.feign;

import com.annakirillova.crmsystem.dto.TrainingInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("trainer-workload-service")
public interface TrainerWorkloadServiceFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/summaries")
    void updateTrainingInfo(@RequestHeader("Authorization") String token,
                            @RequestBody TrainingInfoDto trainingInfoDto);
}
