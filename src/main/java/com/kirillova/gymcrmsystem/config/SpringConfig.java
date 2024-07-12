package com.kirillova.gymcrmsystem.config;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.Training;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan("com.kirillova.gymcrmsystem")
public class SpringConfig {

    @Autowired
    private ConfigurationProperties configProperties;

    @Autowired
    private ConfidentialProperties confidentialProperties;

    @Bean
    public Map<Long, User> userStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Set<String> allUsernames() {
        return Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, TrainingType> trainingTypeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(configProperties.getHibernateDriverClass());
        dataSource.setUrl(configProperties.getHibernateConnectionUrl());
        dataSource.setUsername(configProperties.getHibernateConnectionUsername());
        dataSource.setPassword(confidentialProperties.getHibernateConnectionPassword());

        if (configProperties.isDatabaseInit()) {
            initializeDatabase(dataSource);
        }

        return dataSource;
    }

    private void initializeDatabase(DataSource dataSource) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(configProperties.getJdbcInitLocation())));
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", configProperties.getHibernateDialect());
        properties.put("hibernate.show_sql", configProperties.getHibernateShowSql());
        properties.put("hibernate.current_session_context_class", configProperties.getHibernateCurrentSessionContextClass());

        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.kirillova.gymcrmsystem.models");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }
}