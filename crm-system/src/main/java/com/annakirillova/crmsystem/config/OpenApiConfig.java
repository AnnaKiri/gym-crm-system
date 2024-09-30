package com.annakirillova.crmsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "OAuth2",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost/realms/gym-crm-system-realm/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost/realms/gym-crm-system-realm/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "OpenID Connect"),
                                @OAuthScope(name = "profile", description = "Access profile information"),
                                @OAuthScope(name = "email", description = "Access email")
                        }
                )
        )
)
@OpenAPIDefinition(
        info = @Info(
                title = "CRM System API documentation",
                version = "1.0",
                description = """
                        Gym CRM System application
                        <p><b>Test credentials:</b><br>
                        - Trainee: Angelina.Jolie / password1<br>
                        - Trainer: Brad.Pitt / password6</p>
                        """
        ),
        security = @SecurityRequirement(name = "OAuth2")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("CRM System API")
                .pathsToMatch("/**")
                .build();
    }
}
