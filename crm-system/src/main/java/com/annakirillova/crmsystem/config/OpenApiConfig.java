package com.annakirillova.crmsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Gym CRM System application
                        <p><b>Test credentials:</b><br>
                        - Trainee: Angelina.Jolie / password1<br>
                        - Trainee: Ryan.Reynolds / password2<br>
                        - Trainee: Tom.Hardy / password3<br>
                        - Trainee: Keanu.Reeves / password4<br>
                        <br>
                        - Trainer: Tom.Cruise / password5<br>
                        - Trainer: Brad.Pitt / password6<br>
                        - Trainer: Jennifer.Aniston / password7<br>
                        - Trainer: Sandra.Bullock / password8</p>
                        """
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/**")
                .build();
    }
}
