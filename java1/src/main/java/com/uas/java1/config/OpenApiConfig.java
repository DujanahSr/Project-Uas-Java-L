package com.uas.java1.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Component
@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenApi() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Java Lanjutan API")
                                                .version("0.0.1")
                                                .description("API untuk Uas Java Lanjutan"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))
                                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
        }
}
