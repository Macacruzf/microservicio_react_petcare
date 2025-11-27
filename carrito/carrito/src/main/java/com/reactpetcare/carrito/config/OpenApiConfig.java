package com.reactpetcare.carrito.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Carrito Service",
                version = "1.0",
                description = "Microservicio encargado del carrito de compras en ReactPetCare.",
                contact = @Contact(
                        name = "Equipo ReactPetCare",
                        email = "soporte@reactpetcare.com"
                )
        )
)
public class OpenApiConfig {
}
