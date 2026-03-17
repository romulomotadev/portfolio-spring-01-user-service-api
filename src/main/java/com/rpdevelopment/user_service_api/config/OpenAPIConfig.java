package com.rpdevelopment.user_service_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.security.SecurityRequirement;


@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI userViewAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users Service API")
                        .description("User Reference Project")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 4.0")
                                .url("https://github.com/romulomotadev/portfolio-spring-01-user-service-api")));
    }


    @Bean
    public OpenApiCustomizer globalSecurityOpenApiCustomizer() { // Mudou para Z
        return openApi -> openApi.getPaths().values()
                .forEach(pathItem -> pathItem.readOperations()
                        .forEach(op -> op.addSecurityItem(new SecurityRequirement().addList("bearerAuth")))
                );
    }
}
