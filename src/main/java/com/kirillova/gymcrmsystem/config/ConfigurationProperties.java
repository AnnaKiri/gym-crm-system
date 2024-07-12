package com.kirillova.gymcrmsystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class ConfigurationProperties {

    @Value("${storage.init.data.trainee}")
    private String traineeDataPath;

    @Value("${storage.init.data.trainer}")
    private String trainerDataPath;

    @Value("${storage.init.data.training}")
    private String trainingDataPath;

    @Value("${storage.init.data.trainingtype}")
    private String trainingTypeDataPath;

    @Value("${hibernate.driver_class}")
    private String hibernateDriverClass;

    @Value("${hibernate.connection.url}")
    private String hibernateConnectionUrl;

    @Value("${hibernate.connection.username}")
    private String hibernateConnectionUsername;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.current_session_context_class}")
    private String hibernateCurrentSessionContextClass;

    @Value("${database.init}")
    private boolean databaseInit;

    @Value("${jdbc.initLocation}")
    private String jdbcInitLocation;

}
