package com.annakirillova.crmsystem.integration;

import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.crmsystem.config.KeycloakProperties;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.service.TokenService;
import com.redis.testcontainers.RedisContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static com.annakirillova.crmsystem.UserTestData.USERS_PASSWORDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integration-test")
public abstract class BaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private KeycloakProperties keycloakProperties;

    @PersistenceContext
    protected EntityManager entityManager;

    @ServiceConnection
    private static final PostgreSQLContainer POSTGRESQL = new PostgreSQLContainer("postgres:15.8");

    @ServiceConnection
    private static final ActiveMQContainer ACTIVEMQ = new ActiveMQContainer("apache/activemq-classic:5.18.6");

    private static final KeycloakContainer KEYCLOAK = new KeycloakContainer().withRealmImportFile("keycloak/test-realm.json");

    private static final RedisContainer REDIS = new RedisContainer(DockerImageName.parse("redis:7.4.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("keycloak.url", KEYCLOAK::getAuthServerUrl);
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", REDIS::getFirstMappedPort);
    }

    @BeforeEach
    void resetState() {
        entityManager.clear();
    }

    @BeforeAll
    static void startContainers() {
        POSTGRESQL.start();
        ACTIVEMQ.start();
        KEYCLOAK.start();
        REDIS.start();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected TokenResponseDto getTokensForUser(User user) {
        String username = user.getUsername();
        return tokenService.getUserToken(username, USERS_PASSWORDS.get(username));
    }
}
