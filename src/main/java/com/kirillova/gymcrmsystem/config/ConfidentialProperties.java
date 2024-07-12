package com.kirillova.gymcrmsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:confidential.properties")
public class ConfidentialProperties {
    @Value("${hibernate.connection.password}")
    private String hibernateConnectionPassword;

    public String getHibernateConnectionPassword() {
        return hibernateConnectionPassword;
    }
}