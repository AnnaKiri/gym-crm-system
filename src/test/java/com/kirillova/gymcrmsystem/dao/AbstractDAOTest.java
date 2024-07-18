package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.config.ConfidentialProperties;
import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.config.SpringConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SpringConfig.class,
        ConfigurationProperties.class,
        ConfidentialProperties.class})
@Transactional
public abstract class AbstractDAOTest {
}
