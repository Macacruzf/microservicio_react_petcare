package com.reactpetcare.usuario.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Usuarios Service - API")
                        .description("Microservicio de gestión de usuarios y autenticación con JWT")
                        .version("1.0.0"));
    }
}
