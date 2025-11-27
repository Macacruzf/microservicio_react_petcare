package com.reactpetcare.productos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productosApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Productos Service - API")
                        .description("Microservicio para la gestión de productos y categorías de PetCare")
                        .version("1.0.0"));
    }
}
