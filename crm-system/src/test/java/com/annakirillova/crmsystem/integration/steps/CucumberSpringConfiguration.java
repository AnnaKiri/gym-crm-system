package com.annakirillova.crmsystem.integration.steps;

import com.redis.testcontainers.RedisContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Transactional
@AutoConfigureMockMvc
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class CucumberSpringConfiguration {
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRESQL = new PostgreSQLContainer("postgres:15.8");

    @ServiceConnection
    private static final ActiveMQContainer ACTIVEMQ = new ActiveMQContainer("apache/activemq-classic:5.18.6");

    private static final KeycloakContainer KEYCLOAK = new KeycloakContainer().withRealmImportFile("keycloak/test-realm.json");

    private static final RedisContainer REDIS = new RedisContainer(DockerImageName.parse("redis:7.4.1"));

    @BeforeAll
    public static void startContainers() {
        POSTGRESQL.start();
        ACTIVEMQ.start();
        KEYCLOAK.start();
        REDIS.start();

        System.setProperty("keycloak.url", KEYCLOAK.getAuthServerUrl());
        System.setProperty("spring.data.redis.host", REDIS.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(REDIS.getFirstMappedPort()));
    }
}
