package com.sistemaligafutbol.sistemaligafutbol.security;

import com.sistemaligafutbol.sistemaligafutbol.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        //GESTION DE USUARIOS
                        .requestMatchers("/auth/login").permitAll()

                        //GESTION DE DUENOS
                        .requestMatchers(HttpMethod.POST,"/api/duenos/**").permitAll() // TODOS PUEDEN REGISTRARSE COMO DUENO
                        .requestMatchers(HttpMethod.GET,"/api/duenos").hasRole("ADMIN") //Solo ADMIN puede ver el listado de duenos
                        .requestMatchers(HttpMethod.GET,"/api/duenos/{id}").permitAll() //todos podrian ver a un dueno especifico, por ejemplo al ver un equipo
                        .requestMatchers(HttpMethod.GET,"/api/duenos/porusuario/{idUsuario}").hasRole("DUENO") //solo DUENOS podrian hacer busqueda segun el usuario que son
                        .requestMatchers(HttpMethod.PUT, "/api/duenos/**").hasRole("DUENO") //solo DUENOS podrian modificar sus datos
                        .requestMatchers(HttpMethod.PUT,"/api/duenos/estatus/{idDueno}").hasRole("ADMIN") //SOLO ADMIN puede desactivar DUENOS

                        //GESTION DE ARBITROS
                        .requestMatchers(HttpMethod.POST,"/api/arbitros/**").hasRole("ADMIN") // Solo ADMIN puede REGISTRAR a árbitros
                        .requestMatchers(HttpMethod.GET,"/api/arbitros").hasRole("ADMIN") //solo ADMIN puede ver el listado de arbitros
                        .requestMatchers(HttpMethod.GET,"/api/arbitros/{id}").permitAll()//todos podrian ver a un arbitro especifico, por ejemplo al ver un partido
                        .requestMatchers(HttpMethod.GET,"/api/arbitros/poruser/").hasRole("ARBITRO") //solo ARBITROS podran visualizar sus datos de arbitro segun el usuario que son
                        .requestMatchers(HttpMethod.PUT,"/api/arbitros/actualizar/**").hasRole("ARBITRO") // Solo ARBITROS podrian modificar sus datos
                        .requestMatchers(HttpMethod.PUT,"/api/arbitros/cambiarEstatus/").hasRole("ADMIN") //SOLO ADMIN PODRIA DESACTIVAR A LOS ARBITROS

                        //GESTION DE CAMPOS
                        .requestMatchers(HttpMethod.POST,"/api/campos").hasAnyRole("ADMIN") //SOLO ADMIN puede crear CAMPOS
                        .requestMatchers(HttpMethod.PUT,"/api/campos").hasAnyRole("ADMIN") //SOLO ADMIN puede modificar CAMPOS
                        .requestMatchers(HttpMethod.PUT,"/api/campos/estatus/{idCampo}").hasRole("ADMIN")//SOLO ADMIN puede desactivar los campos
                        .requestMatchers(HttpMethod.GET,"/api/campos").hasRole("ADMIN")//SOLO ADMIN PUEDE VER EL LISTADO DE TODOS LOS CAMPOS
                        .requestMatchers(HttpMethod.GET,"/api/campos/activos").hasAnyRole("ADMIN","DUENO") //TANTO ADMIN como DUENO pueden ver los campos activos, ADMIN para gestion y DUENO cuando quiere seleccionar su campo
                        .requestMatchers(HttpMethod.GET,"/api/campos/{id}").permitAll() //TODOS Pueden ver algun campo en especifico, por ejemplo al ver datos de un partido

                        //GESTION DE CANCHAS
                        .requestMatchers(HttpMethod.POST,"/api/canchas").hasRole("ADMIN") //SOLO ADMIN puede crear canchas
                        .requestMatchers(HttpMethod.PUT,"/api/canchas").hasRole("ADMIN")  //SOLO ADMIN puede modificar canchas
                        .requestMatchers(HttpMethod.PUT,"/api/canchas/estatus/{idCancha}").hasRole("ADMIN") //SOLO ADMIN puede cambiar estatus de la cancha
                        .requestMatchers(HttpMethod.GET,"/api/canchas").hasRole("ADMIN") //SOLO ADMIN puede ver el Listado de todas las canchas
                        .requestMatchers(HttpMethod.GET,"/api/canchas/{id}").permitAll()//TODOS Pueden ver alguna cancha en especifico, por ejemplo al ver datos de un partido

                        //GESTION DE EQUIPOS
                        .requestMatchers(HttpMethod.POST,"/api/equipos").hasRole("DUENO") //SOLO DUENOS pueden crear equipos
                        .requestMatchers(HttpMethod.PUT,"/api/equipos").hasRole("DUENO")  //SOLO DUENOS pueden modificar a su equipo
                        .requestMatchers(HttpMethod.GET,"/api/equipos/**").permitAll() //TODOS podran ver los equipos

                        //GESTION DE JUGADORES
                        .requestMatchers(HttpMethod.POST, "/api/jugadores").hasRole("DUENO") //SOLO DUENOS PUEDEN REGISTRAR JUGADORES
                        .requestMatchers(HttpMethod.PUT,"/api/jugadores/").hasRole("DUENO") //SOLO DUENOS PUEDEN MODIFICAR/desactivar A SUS JUGADORES
                        .requestMatchers(HttpMethod.GET,"/api/jugadores/**").permitAll() // todos necesitarian ver el listado de jugadores
                        .requestMatchers(HttpMethod.GET,"/api/jugadores/credenciales/{idEquipo}/{idTorneo}").permitAll() //tanto ADMIN COMO DUENO PUEDEN DESCARGAR LAS CREDENCIALES POR EQUIPO

                        //GESTION DE TORNEOS
                        .requestMatchers(HttpMethod.POST, "/api/torneos").hasRole("ADMIN") // Solo ADMIN puede REGISTRAR torneos
                        .requestMatchers(HttpMethod.GET,"/api/torneos/**").permitAll() //Todos pueden obtener la info de los torneos
                        .requestMatchers(HttpMethod.PUT,"/api/torneos/**").hasRole("ADMIN") //solo ADMIN puede modificar torneos
                        .requestMatchers(HttpMethod.PATCH, "/api/torneos/{id}/cancelar").hasRole("ADMIN")//solo ADMIN puede cancelar torneos

                        //GESTION DE SOLICITUDES
                        .requestMatchers(HttpMethod.POST,"/api/solicitudes/{idEquipo}/{idTorneo}").hasRole("DUENO") //SOLO DUENOS pueden solicitar unirse a un torneo
                        .requestMatchers(HttpMethod.POST,"/api/solicitudes/admin/agregarEquipo/").hasRole("ADMIN") //solo ADMIN puede agregar equipos directamente al torneo
                        .requestMatchers(HttpMethod.PUT,"/api/solicitudes/**").hasRole("ADMIN") //Solo ADMIN puede aceptar/rechazar las solicitudes
                        .requestMatchers(HttpMethod.GET,"/api/solicitudes/admin/**").hasRole("ADMIN") //solo ADMIN puede ver las solicitudes
                        .requestMatchers(HttpMethod.GET,"/api/solicitudes/dueno/**").hasAnyRole("ADMIN","DUENO") //tanto ADMIN como DUENOS pueden ver las solicitudes por Duenos

                        //GESTION DE CONVOCATORIAS
                        .requestMatchers(HttpMethod.POST,"/api/convocatorias/publicar/{torneoId}").hasRole("ADMIN") //SOLO ADMIN puede publicar convocatorias
                        .requestMatchers(HttpMethod.GET,"/api/convocatorias/activa").permitAll()//TODOS pueden ver la convocatoria ACTIVA

                        //GESTION DE PAGOS
                        .requestMatchers(HttpMethod.PUT,"/api/pagos/admin/**").hasRole("ADMIN") //SOLO ADMIN puede indicar que se hizo una inscripcion, (cancha o arbitraje), y dar prorroga
                        .requestMatchers(HttpMethod.GET,"/api/pagos/admin/todos").hasRole("ADMIN") //SOLO ADMIN PUEDE VER EL LISTADO DE TODOS los pagos
                        .requestMatchers(HttpMethod.GET,"/api/pagos/equipo/**").hasAnyRole("ADMIN","DUENO")//SOLO ADMIN Y DUENOS pueden ver pagos por equipo (todos, arbitraje, cancha)
                        .requestMatchers(HttpMethod.GET, "/api/pagos/todos/precios").permitAll()//todos pueden ver los precios de los tipos de pago

                        //GESTION DE PARTIDOS
                        .requestMatchers(HttpMethod.POST,"/api/partidos/admin/**").hasRole("ADMIN") //SOLO ADMIN puede inciar el torneo (generar partidos)
                        .requestMatchers(HttpMethod.POST,"/api/partidos/arbitro/**").hasRole("ARBITRO") //SOLO ARBITROS pueden registrar resultados de los partidos y demas funciones
                        .requestMatchers(HttpMethod.GET,"/api/partidos/todos/**").permitAll() //todos pueden ver los datos de partidos

                        //GESTION DE JUGADOR_ESTADISTICA
                        .requestMatchers(HttpMethod.POST,"/api/jugadorestadisticas/registrar/").hasRole("ARBITRO")//Solo ARBITROS pueden registrar las estadististicas de los jugadores
                        .requestMatchers(HttpMethod.PUT,"/api/jugadorestadisticas/modificar/").hasRole("ARBITRO")//Solo ARBITROS pueden modificar las estadisticas
                        .requestMatchers(HttpMethod.GET,"/api/jugadorestadisticas/torneo/{idTorneo}").permitAll() //TODOS PODRIAN VER LA TABLA DE GOLEO

                        //GESTION DE CLASIFICACION
                        .requestMatchers(HttpMethod.GET,"/api/tabla-clasificacion/{idTorneo}").permitAll() //TODOS PUEDEN VER LA TABLA DE CLASIFICACION

                        .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}