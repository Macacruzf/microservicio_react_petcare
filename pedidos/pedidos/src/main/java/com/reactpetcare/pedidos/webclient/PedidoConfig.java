package com.reactpetcare.pedidos.webclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PedidoConfig {

    @Bean
    public WebClient carritoWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Bean
    public WebClient productosWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}
