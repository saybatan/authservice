package com.example.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@Configuration
public class SwaggerConfig {

    public final static String API_GATEWAY_LOCAL_DEV_URL = "http://localhost:8080/";

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("authservice")
                .packagesToScan("com.example.authservice.controller")
                .build();
    }

    @Bean
    public OpenAPI appOpenAPI() {
        Server localDevUrl = new Server();
        localDevUrl.setUrl(API_GATEWAY_LOCAL_DEV_URL);

        Info info = new Info()
                .title("Auth Service API")
                .version("1.0")
                .description("API Documentation");
        return new OpenAPI().info(info).servers(List.of(localDevUrl));
    }
}
