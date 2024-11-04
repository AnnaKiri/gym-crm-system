package com.annakirillova.crmsystem.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;

import java.io.IOException;

import static com.annakirillova.common.dto.CredentialRepresentationDto.PASSWORD_STRING;
import static org.zalando.logbook.BodyFilter.merge;
import static org.zalando.logbook.core.BodyFilters.defaultValue;

@Configuration
@RequiredArgsConstructor
public class LogbookConfig {

    private final ObjectMapper objectMapper;

    private static final String CONFIDENTIAL_VALUE = "XXX";
    private static final String CREDENTIALS = "credentials";
    private static final String CREDENTIALS_VALUE = "value";

// it's cause of JsonPathBodyFilter filter operation(s) could not complete,
// the following exception(s) have been thrown: Exception class: java.lang.ClassCastException.
// Message: class com.fasterxml.jackson.databind.node.ArrayNode cannot be cast to class java.util.List
// (com.fasterxml.jackson.databind.node.ArrayNode is in unnamed module of loader 'app'; java.util.List is in module java.base of loader 'bootstrap')
//    @Bean
//    public Logbook logbook() {
//        return Logbook.builder()
//                .bodyFilter(defaultValue())
//                .bodyFilter(jsonPath("$.password").replace("XXX"))
//                .bodyFilter(jsonPath("$.credentials.*.value").replace("XXX"))
//                .build();
//    }
// Workaround is below:

    @Bean
    public BodyFilter bodyFilter() {
        return merge(
                defaultValue(),
                (contentType, body) -> filterSensitiveFields(body)
        );
    }

    private String filterSensitiveFields(String body) {
        try {
            JsonNode rootNode = objectMapper.readTree(body);

            if (rootNode.has(PASSWORD_STRING)) {
                if (rootNode instanceof ObjectNode objectNode) {
                    objectNode.put(PASSWORD_STRING, CONFIDENTIAL_VALUE);
                }
            }

            JsonNode credentialsNode = rootNode.get(CREDENTIALS);
            if (credentialsNode != null && credentialsNode.isArray()) {
                ArrayNode credentialsArray = (ArrayNode) credentialsNode;
                for (JsonNode credential : credentialsArray) {
                    if (credential.has(CREDENTIALS_VALUE)) {
                        ((ObjectNode) credential).put(CREDENTIALS_VALUE, CONFIDENTIAL_VALUE);
                    }
                }
            }

            return objectMapper.writeValueAsString(rootNode);
        } catch (IOException e) {
            return body;
        }
    }
}
