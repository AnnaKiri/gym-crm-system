package com.annakirillova.e2etests.feign;

import com.annakirillova.e2etests.dto.LoginRequestDto;
import com.annakirillova.e2etests.dto.TokenResponseDto;
import com.annakirillova.e2etests.dto.TraineeDto;
import com.annakirillova.e2etests.dto.TrainingDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("crm-system")
public interface CrmSystemFeignClient {

    @PostMapping(value = "/trainees",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LoginRequestDto> register(@Valid @RequestBody TraineeDto traineeDto);

    @PostMapping(value = "/auth/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    TokenResponseDto authenticate(@RequestBody LoginRequestDto loginRequest);

    @PostMapping(value = "/trainings",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void createTraining(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                        @RequestBody TrainingDto trainingDto);

    @DeleteMapping(value = "/trainees/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteTrainee(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                       @PathVariable("username") String username);
}
