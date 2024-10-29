package com.annakirillova.trainerworkloadservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;

@Configuration
public class LogbookConfig {

    @Bean
    public BodyFilter bodyFilter() {
        return null;
    }

}
