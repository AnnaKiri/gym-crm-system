package com.annakirillova.e2etests.feign;

import com.annakirillova.e2etests.dto.TrainerSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("trainer-workload-service")
public interface TrainerWorkloadServiceFeignClient {

    @GetMapping(value = "/trainers/{username}/monthly-summary",
            produces = MediaType.APPLICATION_JSON_VALUE)
    TrainerSummaryDto getMonthlySummary(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @PathVariable("username") String username);
}
