package com.sistemaligafutbol.sistemaligafutbol.config;

import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private static final String IMAGEN_TORNEO = "https://i.imgur.com/WwiUN4K.jpeg";

    @PostConstruct
    public void init() {
        inicializarAdmin();
        inicializarTorneos();
    }

    private void inicializarAdmin() {
        Usuario admin = usuarioRepository.findByEmail(adminEmail);
        if (admin == null) {
            admin = new Usuario();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Set.of("ROLE_ADMIN"));
            admin.setEstatus(true);
            usuarioRepository.save(admin);
        }
    }

    private void inicializarTorneos() {
        // Torneo finalizado
        if (torneoRepository.findByEstatusTorneoFalse().isEmpty()) {
            Torneo torneoFinalizado = new Torneo();
            torneoFinalizado.setNombreTorneo("Torneo Infantil Liga Temixco - Finalizado");
            torneoFinalizado.setDescripcion("Rango de edad: 8-12 años.");
            torneoFinalizado.setFechaInicio(LocalDate.now().minusMonths(3));
            torneoFinalizado.setFechaFin(LocalDate.now().minusWeeks(1));
            torneoFinalizado.setMaxEquipos(16);
            torneoFinalizado.setMinEquipos(8);
            torneoFinalizado.setEquiposLiguilla(8);
            torneoFinalizado.setVueltas(2);
            torneoFinalizado.setLogoTorneo(IMAGEN_TORNEO);
            torneoFinalizado.setEstatusLlenado(true);
            torneoFinalizado.setIniciado(true);
            torneoFinalizado.setEstatusTorneo(false); // Finalizado
            //aqui agregar equipo ganador cuando se haga ese crud
            torneoRepository.save(torneoFinalizado);
        }

        // Torneo en espera
        if (torneoRepository.findByEstatusTorneoTrueAndIniciadoFalse().isEmpty()) {
            Torneo torneoEnEspera = new Torneo();
            torneoEnEspera.setNombreTorneo("Torneo Juvenil Liga Temixco - En Espera");
            torneoEnEspera.setDescripcion("Rango de edad: 13-17 años.");
            torneoEnEspera.setFechaInicio(LocalDate.now().plusWeeks(2)); // Próximo torneo
            torneoEnEspera.setFechaFin(LocalDate.now().plusMonths(3));
            torneoEnEspera.setMaxEquipos(12);
            torneoEnEspera.setMinEquipos(6);
            torneoEnEspera.setEquiposLiguilla(6);
            torneoEnEspera.setVueltas(2);
            torneoEnEspera.setLogoTorneo(IMAGEN_TORNEO);
            torneoEnEspera.setEstatusLlenado(false);
            torneoEnEspera.setIniciado(false);
            torneoEnEspera.setEstatusTorneo(true);
            torneoRepository.save(torneoEnEspera);
        }

        // Torneo en juego
        if (torneoRepository.findByEstatusTorneoTrueAndIniciadoTrue().isEmpty()) {
            Torneo torneoEnJuego = new Torneo();
            torneoEnJuego.setNombreTorneo("Torneo Mayor Liga Temixco - En Juego");
            torneoEnJuego.setDescripcion("Rango de edad: 18+ años.");
            torneoEnJuego.setFechaInicio(LocalDate.now().minusWeeks(6)); // Inició hace 6 semanas
            torneoEnJuego.setFechaFin(LocalDate.now().plusMonths(1));
            torneoEnJuego.setMaxEquipos(20);
            torneoEnJuego.setMinEquipos(10);
            torneoEnJuego.setEquiposLiguilla(8);
            torneoEnJuego.setVueltas(2);
            torneoEnJuego.setLogoTorneo(IMAGEN_TORNEO);
            torneoEnJuego.setEstatusLlenado(true);
            torneoEnJuego.setIniciado(true);
            torneoEnJuego.setEstatusTorneo(true);
            torneoRepository.save(torneoEnJuego);
        }

    }
}
