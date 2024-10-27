package com.annakirillova.crmsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;

@Configuration
@RequiredArgsConstructor
public class LogbookConfig {

    @Bean
    public BodyFilter bodyFilter() {
        return null;
    }

}
