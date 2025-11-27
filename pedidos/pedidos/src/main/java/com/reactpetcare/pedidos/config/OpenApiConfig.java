package com.reactpetcare.pedidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pedidosApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedidos Service - API")
                        .description("Microservicio de Pedidos")
                        .version("1.0.0"));
    }
}
