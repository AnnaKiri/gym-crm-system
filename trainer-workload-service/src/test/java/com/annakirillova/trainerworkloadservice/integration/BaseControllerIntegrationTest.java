package com.annakirillova.trainerworkloadservice.integration;

import com.annakirillova.trainerworkloadservice.config.KeycloakPropertiesExtended;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.MongoDBContainer;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public abstract class BaseControllerIntegrationTest {
    public static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KeycloakPropertiesExtended keycloakProperties;

    @ServiceConnection
    private static final ActiveMQContainer ACTIVEMQ = new ActiveMQContainer("apache/activemq-classic:5.18.6");

    @ServiceConnection
    static MongoDBContainer MONGODB = new MongoDBContainer("mongo:8.0.1");

    private static final KeycloakContainer KEYCLOAK = new KeycloakContainer().withRealmImportFile("keycloak/test-realm.json");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("keycloak.url", KEYCLOAK::getAuthServerUrl);
    }

    @BeforeAll
    static void startContainers() {
        ACTIVEMQ.start();
        MONGODB.start();
        KEYCLOAK.start();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected String getAccessToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", keycloakProperties.getUser().getClientId());
        map.add("client_secret", keycloakProperties.getUser().getClientSecret());
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(keycloakProperties.getUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token", request, Map.class);
        return response.getBody().get("access_token").toString();
    }

}
