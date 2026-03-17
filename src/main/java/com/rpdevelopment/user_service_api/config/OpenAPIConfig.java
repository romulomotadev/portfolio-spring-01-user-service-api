package com.rpdevelopment.user_service_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI userviewAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users Service API")
                        .description("User Reference Project")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 4.0")
                                .url("https://github.com/romulomotadev/portfolio-spring-01-user-service-api")));
    }

}
