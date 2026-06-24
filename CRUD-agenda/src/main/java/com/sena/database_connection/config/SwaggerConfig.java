package com.sena.database_connection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI configurarOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AgendaSENA API")
                        .description("API REST para la gestion de reservas de ambientes del SENA")
                        .version("1.0.0"));
    }
}