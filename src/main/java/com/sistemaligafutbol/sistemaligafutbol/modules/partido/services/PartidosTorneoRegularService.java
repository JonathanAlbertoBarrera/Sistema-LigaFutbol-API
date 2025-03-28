package com.sistemaligafutbol.sistemaligafutbol.modules.partido.services;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.CanchaRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion.Clasificacion;
import com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion.ClasificacionRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.PagoService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.Solicitud;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.ArbitroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartidosTorneoRegularService {

    @Autowired
    private TorneoRepository torneoRepository;
    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private PartidoRepository partidoRepository;
    @Autowired
    private CanchaRepository canchaRepository;
    @Autowired
    private ArbitroRepository arbitroRepository;
    @Autowired
    private PagoService pagoService;
    @Autowired
    private ClasificacionRepository clasificacionRepository;

    //------------GENERAR PARTIDOS DEL TORNEO REGULAR
    @Transactional
    public String iniciarTorneo(Long idTorneo) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        if (torneo.isIniciado()) {
            throw new ValidationException("El torneo ya ha sido iniciado previamente.");
        }

        List<Solicitud> equiposConfirmados = solicitudRepository.findByTorneoAndResolucionTrueAndInscripcionEstatusTrue(torneo);
        int totalEquipos = equiposConfirmados.size();

        if (totalEquipos < torneo.getMinEquipos()) {
            throw new ValidationException("Se necesitan al menos " + torneo.getMinEquipos() + " equipos para iniciar el torneo.");
        }

        if (totalEquipos > torneo.getMaxEquipos()) {
            throw new ValidationException("El torneo ya ha alcanzado el máximo de equipos permitidos.");
        }

        if (totalEquipos % 2 != 0) {
            throw new ValidationException("El número de equipos debe ser par para programar los partidos correctamente. Añade o elimina un equipo.");
        }

        // Ajustar fecha de inicio si es necesario
        LocalDate fechaInicio = torneo.getFechaInicio();
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaInicio.minusDays(3))) {
            fechaInicio = hoy.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            torneo.setFechaInicio(fechaInicio);
        }

        torneo.setIniciado(true);
        torneoRepository.save(torneo);

        // Registra los equipos confirmados en la tabla de clasificación
        for (Solicitud solicitud : equiposConfirmados) {
            Clasificacion clasificacion = new Clasificacion();
            clasificacion.setEquipo(solicitud.getEquipo());
            clasificacion.setTorneo(torneo);
            clasificacion.setGolesAFavor(0); // Inicializa goles
            clasificacion.setGolesEnContra(0); // Inicializa goles contra
            clasificacion.setPartidosEmpatados(0); // Inicializa partidos empatados
            clasificacion.setPartidosGanados(0); // Inicializa partidos ganados
            clasificacion.setPartidosPerdidos(0); // Inicializa partidos perdidos
            clasificacion.setPuntos(0); // Inicializa los puntos
            clasificacion.setAvance(false);//por defecto nadie esta clasificado

            clasificacionRepository.save(clasificacion); // Guarda la clasificación del equipo
        }

        // Generar partidos
        String resultadoGeneracion = generarPartidosFaseRegular(torneo, equiposConfirmados);
        if (!resultadoGeneracion.equals("OK")) {
            throw new ValidationException(resultadoGeneracion);
        }

        // Generar pagos de los partidos
        partidoRepository.findByTorneo(torneo).forEach(pagoService::generarPagoPorPartido);

        return "Torneo iniciado correctamente. Los partidos han sido generados.";
    }

    private String generarPartidosFaseRegular(Torneo torneo, List<Solicitud> equiposConfirmados) {
        List<Equipo> equipos = equiposConfirmados.stream()
                .map(Solicitud::getEquipo)
                .collect(Collectors.toList());

        int totalEquipos = equipos.size();
        int totalJornadas = totalEquipos - 1;
        int vueltas = torneo.getVueltas();

        // FORZAR fecha inicial domingo
        LocalDate fechaPartido = torneo.getFechaInicio().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        List<Equipo> rotacion = new ArrayList<>(equipos);
        Equipo fijo = rotacion.remove(0);
        List<Partido> partidosGenerados = new ArrayList<>();

        try {
            for (int vuelta = 0; vuelta < vueltas; vuelta++) {
                for (int jornada = 0; jornada < totalJornadas; jornada++) {
                    List<Equipo> rotacionActual = new ArrayList<>(rotacion);

                    for (int i = 0; i < totalEquipos / 2; i++) {
                        final Equipo equipoA, equipoB;

                        if (i == 0) {
                            equipoA = fijo;
                            equipoB = rotacionActual.get(totalEquipos - 2);
                        } else {
                            equipoA = rotacionActual.get(i - 1);
                            equipoB = rotacionActual.get(totalEquipos - 2 - i);
                        }

                        final Equipo local, visitante;
                        if ((vuelta % 2 == 0 && jornada % 2 == 0) || (vuelta % 2 == 1 && jornada % 2 == 1)) {
                            local = equipoA;
                            visitante = equipoB;
                        } else {
                            local = equipoB;
                            visitante = equipoA;
                        }

                        // 1. Intento OBLIGATORIO con DOMINGO
                        LocalDate finalFechaPartido = fechaPartido;
                        int finalI1 = i;
                        Optional<Cancha> canchaDomingo = canchaRepository.findByCampo(local.getCampo()).stream()
                                .filter(Cancha::isEstatusCancha)
                                .filter(c -> {
                                    List<Partido> partidos = partidoRepository.findByCanchaAndFechaPartido(c, finalFechaPartido);
                                    return partidos.isEmpty() ||
                                            partidos.stream().noneMatch(p -> p.getHora().equals(LocalTime.of(8 + finalI1 * 2, 0)));
                                })
                                .findFirst();

                        LocalDate finalFechaPartido1 = fechaPartido;
                        int finalI = i;
                        Optional<Arbitro> arbitroDomingo = arbitroRepository.findAll().stream()
                                .filter(a -> a.getUsuario().isEstatus())
                                .filter(a -> {
                                    List<Partido> partidos = partidoRepository.findByArbitroAndFechaPartido(a, finalFechaPartido1);
                                    return partidos.isEmpty() ||
                                            partidos.stream().noneMatch(p -> p.getHora().equals(LocalTime.of(8 + finalI * 2, 0)));
                                })
                                .findFirst();

                        // Verificación EXTRA de disponibilidad
                        if (canchaDomingo.isEmpty()) {
                            System.out.println("No hay cancha disponible para " + local.getNombreEquipo() + " el domingo " + fechaPartido);
                        }
                        if (arbitroDomingo.isEmpty()) {
                            System.out.println("No hay árbitro disponible para " + local.getNombreEquipo() + " el domingo " + fechaPartido);
                        }

                        // SIEMPRE crear en domingo (fallar si no hay recursos)
                        if (canchaDomingo.isEmpty() || arbitroDomingo.isEmpty()) {
                            return "No hay recursos suficientes para generar los partidos el domingo " + fechaPartido;
                        }

                        Partido partido = new Partido();
                        partido.setTorneo(torneo);
                        partido.setEquipoLocal(local);
                        partido.setEquipoVisitante(visitante);
                        partido.setCancha(canchaDomingo.get());
                        partido.setArbitro(arbitroDomingo.get());
                        partido.setFechaPartido(fechaPartido); // DOMINGO OBLIGATORIO
                        partido.setHora(LocalTime.of(8 + i * 2, 0));
                        partido.setTipoPartido("JORNADA REGULAR");
                        partido.setJugado(false);
                        partido.setFinal(false);
                        partidosGenerados.add(partido);
                    }

                    Collections.rotate(rotacion, 1);
                    fechaPartido = fechaPartido.plusWeeks(1); // Siguiente domingo
                }
            }

            partidoRepository.saveAll(partidosGenerados);
            return "OK";
        } catch (Exception e) {
            return "Error inesperado al generar partidos: " + e.getMessage();
        }
    }

}
