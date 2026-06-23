package com.sena.database_connection.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Esta clase configura la documentacion automatica de la API
// Una vez levantado el servidor se puede ver en: http://localhost:8080/swagger-ui.html
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