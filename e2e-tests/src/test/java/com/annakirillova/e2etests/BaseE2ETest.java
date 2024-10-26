package com.annakirillova.e2etests;

import com.annakirillova.e2etests.feign.CrmSystemFeignClient;
import com.annakirillova.e2etests.feign.TrainerWorkloadServiceFeignClient;
import com.redis.testcontainers.RedisContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public abstract class BaseE2ETest {
    protected static final int DELAY_MS = 1000;
    protected static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    protected CrmSystemFeignClient crmSystemFeignClient;

    @Autowired
    protected TrainerWorkloadServiceFeignClient trainerWorkloadServiceFeignClient;

    private static final Network NETWORK = Network.newNetwork();

    private static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:15.8")
            .withNetwork(NETWORK)
            .withNetworkAliases("postgresql");

    private static final ActiveMQContainer ACTIVEMQ = new ActiveMQContainer("apache/activemq-classic:5.18.6")
            .withNetwork(NETWORK)
            .withNetworkAliases("activemq");

    private static final KeycloakContainer KEYCLOAK = new KeycloakContainer().withRealmImportFile("keycloak/test-realm.json")
            .withNetwork(NETWORK)
            .withNetworkAliases("keycloak");

    private static final RedisContainer REDIS = new RedisContainer(DockerImageName.parse("redis:7.4.1"))
            .withNetwork(NETWORK)
            .withNetworkAliases("redis");

    private static final GenericContainer<?> MONGODB = new GenericContainer<>(DockerImageName.parse("mongo:8.0.1"))
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "testuser")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "testpassword")
            .withNetwork(NETWORK)
            .withNetworkAliases("mongodb");

    private static final GenericContainer<?> EUREKA = new GenericContainer<>(DockerImageName.parse("docker.io/steeltoeoss/eureka-server:4.1.1"))
            .withNetwork(NETWORK)
            .withNetworkAliases("eureka");

    private static final GenericContainer<?> CRM_SYSTEM = new GenericContainer<>(DockerImageName.parse("crm-system:1.0.0"))
            .withExposedPorts(80)
            .withEnv("SPRING_PROFILES_ACTIVE", "e2e")
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withNetwork(NETWORK);

    private static final GenericContainer<?> TRAINER_WORKLOAD_SERVICE = new GenericContainer<>(DockerImageName.parse("trainer-workload-service:1.0.0"))
            .withExposedPorts(80)
            .withEnv("SPRING_PROFILES_ACTIVE", "e2e")
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withNetwork(NETWORK);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("crm-system.port", CRM_SYSTEM::getFirstMappedPort);
        registry.add("workload-report-service.port", TRAINER_WORKLOAD_SERVICE::getFirstMappedPort);
    }

    @BeforeAll
    static void startContainers() {
        KEYCLOAK.start();
        EUREKA.start();
        POSTGRESQL.start();
        ACTIVEMQ.start();
        REDIS.start();
        MONGODB.start();
        CRM_SYSTEM.start();
        TRAINER_WORKLOAD_SERVICE.start();
    }

}
