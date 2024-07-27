package com.kirillova.gymcrmsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springdoc.core.configuration.SpringDocFunctionCatalogConfiguration;
import org.springdoc.core.configuration.SpringDocJavadocConfiguration;
import org.springdoc.core.configuration.SpringDocPageableConfiguration;
import org.springdoc.core.configuration.SpringDocSortConfiguration;
import org.springdoc.core.configuration.SpringDocSpecPropertiesConfiguration;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SpringDocConfiguration.class,
        SpringDocConfigProperties.class,
        SpringDocJavadocConfiguration.class,
        SpringDocFunctionCatalogConfiguration.class,
        SpringDocPageableConfiguration.class,
        SpringDocSortConfiguration.class,
        SpringDocSpecPropertiesConfiguration.class,
        SpringDocDataRestConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        MultipleOpenApiSupportConfiguration.class,
        SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiConfigParameters.class,
        SwaggerUiOAuthProperties.class,
        SpringDocUIConfiguration.class
})
@SecuritySchemes({
        @SecurityScheme(
                name = "username",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER,
                paramName = "username"
        ),
        @SecurityScheme(
                name = "password",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER,
                paramName = "password"
        )
})
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Gym CRM System application
                        <p><b>Test credentials:</b><br>
                        - Angelina.Jolie / password1<br>
                        - Ryan.Reynolds / password2<br>
                        - Tom.Hardy / password3<br>
                        - Keanu.Reeves / password4</p>
                        """
        ),
        security = {
                @SecurityRequirement(name = "username"),
                @SecurityRequirement(name = "password")
        }
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
