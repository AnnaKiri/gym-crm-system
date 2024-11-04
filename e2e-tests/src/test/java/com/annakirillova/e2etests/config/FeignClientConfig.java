package com.annakirillova.e2etests.config;

import feign.Feign;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.openfeign.FeignLogbookLogger;

@Configuration
@RequiredArgsConstructor
@EnableFeignClients(basePackages = "com.annakirillova.e2etests.feign")
public class FeignClientConfig {
    private final Logbook logbook;

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .logger(new FeignLogbookLogger(logbook))
                .logLevel(feign.Logger.Level.FULL);
    }
}
