package com.kirillova.gymcrmsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("com.kirillova.gymcrmsystem")
@EnableTransactionManagement
public class SpringConfig {

    @Autowired
    private ConfigurationProperties configProperties;

    @Autowired
    private ConfidentialProperties confidentialProperties;

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
        executeSqlFile(dataSource, configProperties.getJdbcInitLocation());
        executeSqlFile(dataSource, configProperties.getJdbcPopulateLocation());
    }

    private void executeSqlFile(DataSource dataSource, String filePath) {
        ClassLoader classLoader = SpringConfig.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String sql = reader.lines().collect(Collectors.joining("\n"));

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute SQL file: " + filePath, e);
        }
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", configProperties.getHibernateDialect());
        properties.put("hibernate.show_sql", configProperties.getHibernateShowSql());

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