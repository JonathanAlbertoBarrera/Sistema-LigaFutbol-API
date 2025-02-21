package com.sistemaligafutbol.sistemaligafutbol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica CORS a todos los endpoints
                        .allowedOrigins("http://localhost:3000","http://localhost:5173","http://localhost:8081") // Solo permite solicitudes desde estos origenes
                        .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH","OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Encabezados permitidos
                        .allowCredentials(true); // Permite el envío de cookies y credenciales
            }
        };
    }
}
