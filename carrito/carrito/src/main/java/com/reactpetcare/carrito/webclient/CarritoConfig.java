package com.reactpetcare.carrito.webclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CarritoConfig {

    @Bean
    public WebClient productosWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")  // productos
                .build();
    }
}
