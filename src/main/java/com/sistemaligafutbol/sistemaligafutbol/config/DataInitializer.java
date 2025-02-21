package com.sistemaligafutbol.sistemaligafutbol.config;

import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.CampoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.CanchaRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.DuenoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.ArbitroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    CanchaRepository canchaRepository;

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
        inicializarArbitrosYDueno();
        inicializarTorneos();
        inicializarCampos();
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

    private void inicializarCampos() {
        if (campoRepository.count() == 0) { // Solo insertar si no hay registros

            // 🏟️ Campo Galaxy con 2 canchas
            Campo campoGalaxy = new Campo();
            campoGalaxy.setNombre("Campo Galaxy");
            campoGalaxy.setDireccion("Col. Temixco Centro, Calle 22");
            campoGalaxy.setLatitud(18.8525);
            campoGalaxy.setLongitud(-99.2314);
            campoGalaxy.setEstatusCampo(true);
            campoRepository.save(campoGalaxy);

            Cancha cancha1 = new Cancha();
            cancha1.setNumeroCancha(1);
            cancha1.setDescripcion("Cancha techada");
            cancha1.setEstatusCancha(true);
            cancha1.setCampo(campoGalaxy);

            Cancha cancha2 = new Cancha();
            cancha2.setNumeroCancha(2);
            cancha2.setDescripcion("Cancha de césped sintético");
            cancha2.setEstatusCancha(true);
            cancha2.setCampo(campoGalaxy);

            canchaRepository.saveAll(List.of(cancha1, cancha2));

            // ⚽ Campo El Guaje con 1 cancha
            Campo campoGuaje = new Campo();
            campoGuaje.setNombre("El Guaje");
            campoGuaje.setDireccion("Avenida Futbolistas 456");
            campoGuaje.setLatitud(18.8471);
            campoGuaje.setLongitud(-99.2205);
            campoGuaje.setEstatusCampo(true);
            campoRepository.save(campoGuaje);

            Cancha cancha3 = new Cancha();
            cancha3.setNumeroCancha(1);
            cancha3.setDescripcion("Cancha de tierra");
            cancha3.setEstatusCancha(true);
            cancha3.setCampo(campoGuaje);

            canchaRepository.save(cancha3);

            // 🏟️ Campo Azteca sin canchas y deshabilitado
            Campo campoAzteca = new Campo();
            campoAzteca.setNombre("Campo Azteca");
            campoAzteca.setDireccion("Plaza Azteca 789");
            campoAzteca.setLatitud(18.8403);
            campoAzteca.setLongitud(-99.2158);
            campoAzteca.setEstatusCampo(false);
            campoRepository.save(campoAzteca);
        }
    }

    private void inicializarArbitrosYDueno() {
        if (usuarioRepository.countByRolesContainingIgnoreCase("ROLE_ARBITRO") == 0) { // Verifica si hay árbitros registrados

            // 🟢 Árbitro 1
            Usuario usuarioArbitro1 = new Usuario();
            usuarioArbitro1.setEmail("arbitro1@gmail.com");
            usuarioArbitro1.setPassword(passwordEncoder.encode("arbi123"));
            usuarioArbitro1.setEstatus(true);
            usuarioArbitro1.setRoles(Set.of("ROLE_ARBITRO"));
            usuarioRepository.save(usuarioArbitro1);

            Arbitro arbitro1 = new Arbitro();
            arbitro1.setNombreCompleto("Arbitro 1");
            arbitro1.setImagenUrl("https://i.imgur.com/cuIjks0.jpeg");
            arbitro1.setUsuario(usuarioArbitro1);
            arbitroRepository.save(arbitro1);

            // 🟢 Árbitro 2
            Usuario usuarioArbitro2 = new Usuario();
            usuarioArbitro2.setEmail("arbitro2@gmail.com");
            usuarioArbitro2.setPassword(passwordEncoder.encode("arbi456"));
            usuarioArbitro2.setEstatus(true);
            usuarioArbitro2.setRoles(Set.of("ROLE_ARBITRO"));
            usuarioRepository.save(usuarioArbitro2);

            Arbitro arbitro2 = new Arbitro();
            arbitro2.setNombreCompleto("Arbitro 2");
            arbitro2.setImagenUrl("https://i.imgur.com/cuIjks0.jpeg");
            arbitro2.setUsuario(usuarioArbitro2);
            arbitroRepository.save(arbitro2);
        }

        if (usuarioRepository.countByRolesContainingIgnoreCase("ROLE_DUENO") == 0) { // Verifica si hay dueños registrados

            // 🟠 Dueño 1
            Usuario usuarioDueno1 = new Usuario();
            usuarioDueno1.setEmail("dueno1@gmail.com");
            usuarioDueno1.setPassword(passwordEncoder.encode("d123"));
            usuarioDueno1.setEstatus(true);
            usuarioDueno1.setRoles(Set.of("ROLE_DUENO"));
            usuarioRepository.save(usuarioDueno1);

            Dueno dueno1 = new Dueno();
            dueno1.setNombreCompleto("Dueno 1");
            dueno1.setImagenUrl("https://i.imgur.com/BClPFAt.jpeg");
            dueno1.setUsuario(usuarioDueno1);
            //aqui se setearia el equipo cuando se tenga ese crud
            duenoRepository.save(dueno1);

            // 🟠 Dueño 2
            Usuario usuarioDueno2 = new Usuario();
            usuarioDueno2.setEmail("dueno2@gmail.com");
            usuarioDueno2.setPassword(passwordEncoder.encode("d456"));
            usuarioDueno2.setEstatus(true);
            usuarioDueno2.setRoles(Set.of("ROLE_DUENO"));
            usuarioRepository.save(usuarioDueno2);

            Dueno dueno2 = new Dueno();
            dueno2.setNombreCompleto("Dueno 2");
            dueno2.setImagenUrl("https://i.imgur.com/BClPFAt.jpeg");
            dueno2.setUsuario(usuarioDueno2);
            //aqui se setearia el equipo cuando se tenga ese crud
            duenoRepository.save(dueno2);
        }
    }


}
