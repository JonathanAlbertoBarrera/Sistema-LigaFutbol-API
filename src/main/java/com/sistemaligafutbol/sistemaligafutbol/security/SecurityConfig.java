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
                        //GESTION DE USUARIOS
                        .requestMatchers("/auth/login").permitAll()

                        //GESTION DE DUENOS
                        .requestMatchers(HttpMethod.POST,"/api/duenos/**").permitAll() // TODOS PUEDEN REGISTRARSE COMO DUENO
                        .requestMatchers(HttpMethod.GET,"/api/duenos").hasRole("ADMIN") //Solo ADMIN puede ver el listado de duenos
                        .requestMatchers(HttpMethod.GET,"/api/duenos/{id}").permitAll() //todos podrian ver a un dueno especifico, por ejemplo al ver un equipo
                        .requestMatchers(HttpMethod.PUT, "/api/duenos").hasRole("DUENO") //solo DUENOS podrian modificar sus datos

                        //GESTION DE ARBITROS
                        .requestMatchers(HttpMethod.POST,"/api/arbitros").hasRole("ADMIN") // Solo ADMIN puede REGISTRAR a árbitros
                        .requestMatchers(HttpMethod.GET,"/api/arbitros").hasRole("ADMIN") //solo ADMIN puede ver el listado de arbitros
                        .requestMatchers(HttpMethod.GET,"/api/arbitros/{id}").permitAll()//todos podrian ver a un arbitro especifico, por ejemplo al ver un partido
                        .requestMatchers(HttpMethod.PUT,"/api/arbitros").hasRole("ARBITRO") // Solo ARBITROS podrian modificar sus datos

                        //GESTION DE TORNEOS
                        .requestMatchers(HttpMethod.POST, "/api/torneos").hasRole("ADMIN") // Solo ADMIN puede REGISTRAR torneos
                        .requestMatchers(HttpMethod.GET,"/api/torneos/**").permitAll() //Todos pueden obtener la info de los torneos
                        .requestMatchers(HttpMethod.PUT,"/api/torneos/**").hasRole("ADMIN") //solo ADMIN puede modificar torneos
                        .requestMatchers(HttpMethod.PATCH, "/api/torneos/{id}/cancelar").hasRole("ADMIN")//solo ADMIN puede cancelar torneos

                        //GESTION DE CAMPOS
                        .requestMatchers(HttpMethod.POST,"/api/campos").hasAnyRole("ADMIN") //SOLO ADMIN puede crear CAMPOS
                        .requestMatchers(HttpMethod.PUT,"/api/campos").hasAnyRole("ADMIN") //SOLO ADMIN puede modificar CAMPOS
                        .requestMatchers(HttpMethod.GET,"/api/campos").hasRole("ADMIN")//SOLO ADMIN PUEDE VER EL LISTADO DE TODOS LOS CAMPOS
                        .requestMatchers(HttpMethod.GET,"/api/campos/activos").hasAnyRole("ADMIN","DUENO") //TANTO ADMIN como DUENO pueden ver los campos activos, ADMIN para gestion y DUENO cuando quiere seleccionar su campo
                        .requestMatchers(HttpMethod.GET,"/api/campos/{id}").permitAll() //TODOS Pueden ver algun campo en especifico, por ejemplo al ver datos de un partido

                        //GESTION DE CANCHAS
                        .requestMatchers(HttpMethod.POST,"/api/canchas").hasRole("ADMIN") //SOLO ADMIN puede crear canchas
                        .requestMatchers(HttpMethod.PUT,"/api/canchas").hasRole("ADMIN")  //SOLO ADMIN puede modificar canchas
                        .requestMatchers(HttpMethod.GET,"/api/canchas").hasRole("ADMIN") //SOLO ADMIN puede ver el Listado de todas las canchas
                        .requestMatchers(HttpMethod.GET,"/api/canchas/{id}").permitAll()//TODOS Pueden ver alguna cancha en especifico, por ejemplo al ver datos de un partido

                        //GESTION DE EQUIPOS
                                .requestMatchers(HttpMethod.POST,"/api/equipos").hasRole("DUENO") //SOLO DUENOS pueden crear equipos
                                .requestMatchers(HttpMethod.PUT,"/api/equipos").hasRole("DUENO")  //SOLO DUENOS pueden modificar a su equipo
                                .requestMatchers(HttpMethod.GET,"/api/equipos/**").permitAll() //TODOS podran ver los equipos

                                //GESTION DE SOLICITUDES
                                .requestMatchers(HttpMethod.POST,"/api/solicitudes/{idEquipo}/{idTorneo}").hasRole("DUENO") //SOLO DUENOS pueden solicitar unirse a un torneo
                                .requestMatchers(HttpMethod.POST,"/api/solicitudes/admin/agregarEquipo/").hasRole("ADMIN") //solo ADMIN puede agregar equipos directamente al torneo
                                .requestMatchers(HttpMethod.PUT,"/api/solicitudes/**").hasRole("ADMIN") //Solo ADMIN puede aceptar/rechazar las solicitudes
                                .requestMatchers(HttpMethod.GET,"/api/solicitudes/admin/**").hasRole("ADMIN") //solo ADMIN puede ver las solicitudes
                                .requestMatchers(HttpMethod.GET,"/api/solicitudes/dueno/**").hasAnyRole("ADMIN","DUENO") //tanto ADMIN como DUENOS pueden ver las solicitudes por Duenos

                                //GESTION DE JUGADORES
                        .requestMatchers(HttpMethod.POST, "/api/jugadores").hasRole("DUENO") //SOLO DUENOS PUEDEN REGISTRAR JUGADORES
                        .requestMatchers(HttpMethod.PUT,"/api/jugadores/").hasRole("DUENO") //SOLO DUENOS PUEDEN MODIFICAR/desactivar A SUS JUGADORES
                        .requestMatchers(HttpMethod.GET,"/api/jugadores/**").permitAll() // todos necesitarian ver el listado de jugadores

                        //GESTION DE PARTIDOS
                        .requestMatchers(HttpMethod.POST,"/api/partidos/iniciartorneo/").hasRole("ADMIN") //SOLO ADMIN puede inciar el torneo (generar partidos)
                        .requestMatchers(HttpMethod.POST,"/api/partidos/iniciarliguilla/").hasRole("ADMIN") //SOLO ADMIN puede iniciar la liguilla (generar partidos)
                        .requestMatchers(HttpMethod.POST,"/api/partidos/registraresultado/").hasRole("ARBITRO") //SOLO ARBITROS pueden registrar resultados de los partidos
                        .requestMatchers(HttpMethod.PUT,"/api/partidos/modificar/").hasRole("ADMIN")//SOLO ADMIN puede modificar los partidos

                        //GESTION DE JUGADOR_ESTADISTICA
                        .requestMatchers(HttpMethod.POST,"/api/jugadorestadisticas/registrar/").hasRole("ARBITRO")//Solo ARBITROS pueden registrar las estadististicas de los jugadores
                        .requestMatchers(HttpMethod.PUT,"/api/jugadorestadisticas/modificar/").hasRole("ARBITRO")//Solo ARBITROS pueden modificar las estadisticas

                        //GESTION DE PAGOS
                        .requestMatchers(HttpMethod.PUT,"/api/pagos/admin/**").hasRole("ADMIN") //SOLO ADMIN puede indicar que se hizo una inscripcion, (cancha o arbitraje), y dar prorroga
                        .requestMatchers(HttpMethod.GET,"/api/pagos/equipo/**").hasAnyRole("ADMIN","DUENO")//SOLO ADMIN Y DUENOS pueden ver pagos por equipo (todos, arbitraje, cancha)
                        .requestMatchers(HttpMethod.GET, "/api/pagos/todos/precios").permitAll()//todos pueden ver los precios de los tipos de pago

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