package com.annakirillova.trainerworkloadservice.integration.steps;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.MongoDBContainer;

@AutoConfigureMockMvc
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class CucumberSpringConfiguration {

    @ServiceConnection
    private static final ActiveMQContainer ACTIVEMQ = new ActiveMQContainer("apache/activemq-classic:5.18.6");

    @ServiceConnection
    private static final MongoDBContainer MONGODB = new MongoDBContainer("mongo:8.0.1");

    private static final KeycloakContainer KEYCLOAK = new KeycloakContainer().withRealmImportFile("keycloak/test-realm.json");

    @BeforeAll
    public static void startContainers() {
        KEYCLOAK.start();
        ACTIVEMQ.start();
        MONGODB.start();

        System.setProperty("keycloak.url", KEYCLOAK.getAuthServerUrl());
    }
}
