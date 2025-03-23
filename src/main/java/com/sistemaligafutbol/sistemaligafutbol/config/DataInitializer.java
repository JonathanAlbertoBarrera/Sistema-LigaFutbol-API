package com.sistemaligafutbol.sistemaligafutbol.config;

import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.CampoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.CanchaRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.JugadorRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos.ConfiguracionPago;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos.ConfiguracionPagoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.Solicitud;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
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
import java.util.*;

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
    private CanchaRepository canchaRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfiguracionPagoRepository configuracionPagoRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private static final String IMAGEN_TORNEO = "https://drive.google.com/uc?id=1s2_5cOUsoZJdFRcJ5naVrwwfuZfC8zSl";
    private static final String IMAGEN_EQUIPO = "https://drive.google.com/uc?id=1y-o9JC_XeMfFB-RudAoY_VKULu0v5tfD";
    private static final String IMAGEN_DUENO = "https://drive.google.com/uc?id=1F5_9rA24pBjsqNCTJxAwKMafhSJdevlK";
    private static final String IMAGEN_ARBITRO = "https://drive.google.com/uc?id=1202Hru_FxrouKM9_UYyfyO3CNycu1TQv";
    private static final String IMAGEN_JUGADOR = "https://drive.google.com/uc?id=1HPoAhNtSwvPAUSo_8Hite99YBqGD-omE";

    @PostConstruct
    public void init() {
        inicializarAdmin();
        inicializarArbitrosYDuenos();
        inicializarTorneos();
        inicializarCampos();
        inicializarEquipos();
        inicializarJugadores();
        inicializarSolicitudes();
        inicializarPagos();
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

    private void inicializarPagos(){
        if(configuracionPagoRepository.count()==0){
            ConfiguracionPago pago=new ConfiguracionPago();
            pago.setTipoPago("Inscripción");
            pago.setMonto(850.00);
            configuracionPagoRepository.save(pago);

            ConfiguracionPago pagoA=new ConfiguracionPago();
            pagoA.setTipoPago("Arbitraje");
            pagoA.setMonto(150.00);
            configuracionPagoRepository.save(pagoA);

            ConfiguracionPago pagoC=new ConfiguracionPago();
            pagoC.setTipoPago("Cancha");
            pagoC.setMonto(200.00);
            configuracionPagoRepository.save(pagoC);
        }
    }

    private void inicializarArbitrosYDuenos() {
        // 🟢 Árbitros
        if (usuarioRepository.countByRolesContainingIgnoreCase("ROLE_ARBITRO") == 0) {
            for (int i = 1; i <= 3; i++) {
                Usuario usuarioArbitro = new Usuario();
                usuarioArbitro.setEmail("arbitro" + i + "@gmail.com");
                usuarioArbitro.setPassword(passwordEncoder.encode("arbi123"));
                usuarioArbitro.setEstatus(true);
                usuarioArbitro.setRoles(Set.of("ROLE_ARBITRO"));
                usuarioRepository.save(usuarioArbitro);

                Arbitro arbitro = new Arbitro();
                arbitro.setNombreCompleto("Arbitro " + i);
                arbitro.setImagenUrl(IMAGEN_ARBITRO);
                arbitro.setUsuario(usuarioArbitro);
                arbitroRepository.save(arbitro);
            }
        }

        // 🟠 Dueños
        if (usuarioRepository.countByRolesContainingIgnoreCase("ROLE_DUENO") == 0) {
            for (int i = 1; i <= 8; i++) {
                Usuario usuarioDueno = new Usuario();
                usuarioDueno.setEmail("dueno" + i + "@gmail.com");
                usuarioDueno.setPassword(passwordEncoder.encode("d123"));
                usuarioDueno.setEstatus(true);
                usuarioDueno.setRoles(Set.of("ROLE_DUENO"));
                usuarioRepository.save(usuarioDueno);

                Dueno dueno = new Dueno();
                dueno.setNombreCompleto("Dueno " + i);
                dueno.setImagenUrl(IMAGEN_DUENO);
                dueno.setUsuario(usuarioDueno);
                duenoRepository.save(dueno);
            }
        }
    }

    private void inicializarTorneos() {
        // Torneo finalizado (Sub-12)
        if (torneoRepository.findByNombreTorneo("Torneo Sub-12 Finalizado").isEmpty()) {
            Torneo torneoFinalizado = new Torneo();
            torneoFinalizado.setNombreTorneo("Torneo Sub-12 Finalizado");
            torneoFinalizado.setDescripcion("Torneo para menores de 12 años.");
            torneoFinalizado.setFechaInicio(LocalDate.now().minusMonths(3));
            torneoFinalizado.setFechaFin(LocalDate.now().minusWeeks(1));
            torneoFinalizado.setMaxEquipos(6);
            torneoFinalizado.setMinEquipos(4);
            torneoFinalizado.setEquiposLiguilla(4);
            torneoFinalizado.setVueltas(2);
            torneoFinalizado.setLogoTorneo(IMAGEN_TORNEO);
            torneoFinalizado.setPremio("Medallas individuales");
            torneoFinalizado.setEstatusLlenado(true);
            torneoFinalizado.setIniciado(true);
            torneoFinalizado.setEsliguilla(false);
            torneoFinalizado.setEstatusTorneo(false); // Finalizado
            torneoRepository.save(torneoFinalizado);
        }

        // Torneo en juego (Sub-15)
        if (torneoRepository.findByNombreTorneo("Torneo Sub-15 En Juego").isEmpty()) {
            Torneo torneoEnJuego = new Torneo();
            torneoEnJuego.setNombreTorneo("Torneo Sub-15 En Juego");
            torneoEnJuego.setDescripcion("Torneo para menores de 15 años.");
            torneoEnJuego.setFechaInicio(LocalDate.now().minusWeeks(6));
            torneoEnJuego.setFechaFin(LocalDate.now().plusWeeks(1));
            torneoEnJuego.setMaxEquipos(8);
            torneoEnJuego.setMinEquipos(4);
            torneoEnJuego.setEquiposLiguilla(4);
            torneoEnJuego.setVueltas(1);
            torneoEnJuego.setLogoTorneo(IMAGEN_TORNEO);
            torneoEnJuego.setPremio("5000 pesos por equipo + medallas");
            torneoEnJuego.setEstatusLlenado(true);
            torneoEnJuego.setIniciado(true);
            torneoEnJuego.setEsliguilla(true);
            torneoEnJuego.setEstatusTorneo(true);
            torneoRepository.save(torneoEnJuego);
        }

        // Torneo en espera (Sub-17)
        if (torneoRepository.findByNombreTorneo("Torneo Sub-17 En Espera").isEmpty()) {
            Torneo torneoEnEspera = new Torneo();
            torneoEnEspera.setNombreTorneo("Torneo Sub-17 En Espera");
            torneoEnEspera.setDescripcion("Torneo para menores de 17 años.");
            torneoEnEspera.setFechaInicio(LocalDate.now().plusWeeks(2));
            torneoEnEspera.setFechaFin(LocalDate.now().plusMonths(3));
            torneoEnEspera.setMaxEquipos(6);
            torneoEnEspera.setMinEquipos(4);
            torneoEnEspera.setEquiposLiguilla(4);
            torneoEnEspera.setVueltas(1);
            torneoEnEspera.setLogoTorneo(IMAGEN_TORNEO);
            torneoEnEspera.setPremio("15,000 pesos para el equipo + medallas individuales");
            torneoEnEspera.setEstatusLlenado(false);
            torneoEnEspera.setIniciado(false);
            torneoEnEspera.setEsliguilla(false);
            torneoEnEspera.setEstatusTorneo(true);
            torneoRepository.save(torneoEnEspera);
        }
    }

    private void inicializarCampos() {
        if (campoRepository.count() == 0) {
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
        }
    }

    private void inicializarEquipos() {
        if (equipoRepository.count() == 0) {
            // Lista de nombres de equipos famosos
            String[] nombresEquipos = {"Barcelona", "Real Madrid", "Manchester United", "Juventus", "Bayern Munich", "Paris Saint-Germain", "Chelsea", "Liverpool"};

            // Campo
            Campo campoGalaxy = campoRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Campo Galaxy no encontrado"));

            // Crear equipos para cada dueño
            for (int i = 1; i <= 8; i++) {
                Dueno dueno = duenoRepository.findById((long) i)
                        .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));

                // Cada dueño tiene 2 o 3 equipos en diferentes categorías
                for (int j = 0; j < 3; j++) {
                    String categoria = j == 0 ? "Sub-12" : (j == 1 ? "Sub-15" : "Sub-17");
                    Equipo equipo = new Equipo();
                    equipo.setNombreEquipo(nombresEquipos[i - 1] + " " + categoria);
                    equipo.setLogo(IMAGEN_EQUIPO);
                    equipo.setDueno(dueno);
                    equipo.setCampo(campoGalaxy);
                    equipoRepository.save(equipo);
                }
            }
        }
    }

    private void inicializarJugadores() {
        if (jugadorRepository.count() == 0) {
            List<Equipo> equipos = equipoRepository.findAll();

            for (Equipo equipo : equipos) {
                int numJugadores = (int) (Math.random() * 10) + 11; // Entre 11 y 20 jugadores por equipo

                // Determinar la categoría del equipo
                String nombreEquipo = equipo.getNombreEquipo();
                String categoria = nombreEquipo.substring(nombreEquipo.lastIndexOf(" ") + 1); // Obtiene la última parte del nombre (Sub-12, Sub-15, etc.)

                // Calcular la fecha de nacimiento según la categoría
                LocalDate fechaNacimientoBase;
                switch (categoria) {
                    case "Sub-12":
                        fechaNacimientoBase = LocalDate.now().minusYears(12); // Jugadores menores de 12 años
                        break;
                    case "Sub-15":
                        fechaNacimientoBase = LocalDate.now().minusYears(15); // Jugadores menores de 15 años
                        break;
                    case "Sub-17":
                        fechaNacimientoBase = LocalDate.now().minusYears(17); // Jugadores menores de 17 años
                        break;
                    default:
                        throw new IllegalArgumentException("Categoría no válida: " + categoria);
                }

                for (int i = 1; i <= numJugadores; i++) {
                    Jugador jugador = new Jugador();
                    jugador.setNombreCompleto("Jugador " + i + " - " + equipo.getNombreEquipo());

                    // Fecha de nacimiento aleatoria dentro del rango de la categoría
                    LocalDate fechaNacimiento = fechaNacimientoBase.minusDays((long) (Math.random() * 365)); // +/- 1 año
                    jugador.setFechaNacimiento(fechaNacimiento);

                    jugador.setFotoJugador(IMAGEN_JUGADOR);
                    jugador.setNumeroCamiseta(i);
                    jugador.setPartidosJugados(0);
                    jugador.setHabilitado(true);
                    jugador.setEquipo(equipo);
                    jugadorRepository.save(jugador);
                }
            }
        }
    }

    private void inicializarSolicitudes() {
        if (solicitudRepository.count() == 0) {
            List<Torneo> torneos = torneoRepository.findAll();
            List<Equipo> equipos = equipoRepository.findAll();

            // Organizar equipos por categoría
            Map<String, List<Equipo>> equiposPorCategoria = new HashMap<>();
            for (Equipo equipo : equipos) {
                String nombreEquipo = equipo.getNombreEquipo();
                String categoria = nombreEquipo.substring(nombreEquipo.lastIndexOf(" ") + 1); // Extraer la categoría
                equiposPorCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(equipo);
            }

            // Asignar equipos a torneos de la misma categoría
            for (Torneo torneo : torneos) {
                String categoriaTorneo = torneo.getNombreTorneo().contains("Sub-12") ? "Sub-12" :
                        torneo.getNombreTorneo().contains("Sub-15") ? "Sub-15" :
                                "Sub-17";

                List<Equipo> equiposCategoria = equiposPorCategoria.get(categoriaTorneo);
                if (equiposCategoria != null) {
                    int maxEquiposTorneo = torneo.getMaxEquipos(); // Obtener el máximo de equipos del torneo
                    int equiposAceptados = 0;

                    for (int i = 0; i < equiposCategoria.size(); i++) {
                        Equipo equipo = equiposCategoria.get(i);
                        Solicitud solicitud = new Solicitud();
                        solicitud.setEquipo(equipo);
                        solicitud.setTorneo(torneo);

                        // Lógica para determinar el estado de la solicitud
                        if (categoriaTorneo.equals("Sub-12")) {
                            if (i < 6) { // Primeros 6 equipos aceptados
                                solicitud.setInscripcionEstatus(true);
                                solicitud.setResolucion(true);
                                solicitud.setPendiente(false);
                            } else { // Los últimos 2 equipos rechazados
                                solicitud.setInscripcionEstatus(false);
                                solicitud.setResolucion(false);
                                solicitud.setPendiente(false);
                            }
                        } else if (categoriaTorneo.equals("Sub-15")) {
                            // Todos los equipos aceptados
                            solicitud.setInscripcionEstatus(true);
                            solicitud.setResolucion(true);
                            solicitud.setPendiente(false);
                        } else if (categoriaTorneo.equals("Sub-17")) {
                            if (i < 4) { // Primeros 4 equipos aceptados
                                solicitud.setInscripcionEstatus(true);
                                solicitud.setResolucion(true);
                                solicitud.setPendiente(false);
                            } else if (i == 4) { // Quinto equipo pendiente
                                solicitud.setInscripcionEstatus(false);
                                solicitud.setResolucion(false);
                                solicitud.setPendiente(true);
                            } else { // Sexto equipo rechazado
                                solicitud.setInscripcionEstatus(false);
                                solicitud.setResolucion(false);
                                solicitud.setPendiente(false);
                            }
                        }

                        solicitudRepository.save(solicitud);
                    }
                }
            }
        }
    }


}