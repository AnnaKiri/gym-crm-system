package com.kirillova.gymcrmsystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class ConfigurationProperties {

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

    @Value("classpath:db/populateDB.sql")
    private String jdbcPopulateLocation;
}
