package com.sistemaligafutbol.sistemaligafutbol.security;

import com.sistemaligafutbol.sistemaligafutbol.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        //CREACION DE USUARIOS
                        .requestMatchers(HttpMethod.POST,"/api/duenos/**").permitAll() // TODOS PUEDEN REGISTRARSE COMO ADMIN
                        .requestMatchers(HttpMethod.POST,"/api/arbitros").hasRole("ADMIN") // Solo ADMIN puede REGISTRAR a árbitros

                        //CRUD DE JUGADORES
                        .requestMatchers(HttpMethod.POST, "/api/jugadores").hasRole("DUENO") //SOLO DUENOS PUEDEN REGISTRAR JUGADORES
                        .requestMatchers(HttpMethod.PUT,"/api/jugadores/").hasRole("DUENO") //SOLO DUENOS PUEDEN MODIFICAR A SUS JUGADORES
                        .requestMatchers(HttpMethod.GET,"/api/jugadores/**").permitAll() // todos necesitarian ver el listado de jugadores
                        .requestMatchers(HttpMethod.DELETE,"/api/jugadores").hasAnyRole("ADMIN","DUENO")


                        .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin sesiones
                );

        // Agregar el filtro JWT antes del filtro de autenticación
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}