package com.sistemaligafutbol.sistemaligafutbol.modules.partido.services;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.CanchaRepository;
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
        LocalDate fechaPartido = torneo.getFechaInicio();

        List<Equipo> rotacion = new ArrayList<>(equipos);
        Equipo fijo = rotacion.remove(0); // El primer equipo se mantiene fijo en el round-robin

        List<Partido> partidosGenerados = new ArrayList<>();

        try {
            for (int vuelta = 0; vuelta < vueltas; vuelta++) {
                for (int jornada = 0; jornada < totalJornadas; jornada++) {
                    List<Equipo> rotacionActual = new ArrayList<>(rotacion); // Copia para evitar modificaciones en la lambda

                    for (int i = 0; i < totalEquipos / 2; i++) {
                        final Equipo equipoA, equipoB;

                        if (i == 0) {
                            equipoA = fijo;
                            equipoB = rotacionActual.get(totalEquipos - 2);
                        } else {
                            equipoA = rotacionActual.get(i - 1);
                            equipoB = rotacionActual.get(totalEquipos - 2 - i);
                        }

                        // Alternancia en la segunda vuelta
                        final Equipo local, visitante;
                        if ((vuelta % 2 == 0 && jornada % 2 == 0) || (vuelta % 2 == 1 && jornada % 2 == 1)) {
                            local = equipoA;
                            visitante = equipoB;
                        } else {
                            local = equipoB;
                            visitante = equipoA;
                        }

                        final LocalDate fechaJuego = fechaPartido;

                        // Buscar cancha disponible
                        Optional<Cancha> canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream()
                                .filter(c -> c.isEstatusCancha())
                                .filter(c -> partidoRepository.findByCanchaAndFechaPartido(c, fechaJuego).isEmpty())
                                .findFirst();

                        // Buscar árbitro activo y disponible
                        Optional<Arbitro> arbitroDisponible = arbitroRepository.findAll().stream()
                                .filter(a -> a.getUsuario().isEstatus()) // Solo árbitros activos
                                .filter(a -> partidoRepository.findByArbitroAndFechaPartido(a, fechaJuego).isEmpty()) // 🔥 Solo árbitros sin partido ese día
                                .findFirst();

                        // Si no hay disponibilidad en domingo, probar en sábado
                        final LocalDate fechaAjustada = canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()
                                ? fechaJuego.minusDays(1) // Intentar el sábado
                                : fechaJuego;

                        if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                            canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream()
                                    .filter(c -> partidoRepository.findByCanchaAndFechaPartido(c, fechaAjustada).isEmpty())
                                    .findFirst();
                            arbitroDisponible = arbitroRepository.findAll().stream()
                                    .filter(a -> partidoRepository.findByArbitroAndFechaPartido(a, fechaAjustada).isEmpty())
                                    .findFirst();
                        }

                        // Si sigue sin haber disponibilidad, no se pueden generar los partidos
                        if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                            return "No hay suficientes recursos para generar los partidos.";
                        }

                        // Crear partido
                        Partido partido = new Partido();
                        partido.setTorneo(torneo);
                        partido.setEquipoLocal(local);
                        partido.setEquipoVisitante(visitante);
                        partido.setCancha(canchaDisponible.get());
                        partido.setArbitro(arbitroDisponible.get());
                        partido.setFechaPartido(fechaAjustada);
                        partido.setHora(LocalTime.of(10 + i * 2, 0));
                        partido.setJugado(false);

                        partidosGenerados.add(partido);
                        pagoService.generarPagoPorPartido(partido);
                    }

                    // Rotar la lista de equipos para el siguiente enfrentamiento
                    Collections.rotate(rotacion, 1);
                    fechaPartido = fechaPartido.plusWeeks(1);
                }
            }

            partidoRepository.saveAll(partidosGenerados);
            return "OK";
        } catch (Exception e) {
            return "Error inesperado al generar partidos: " + e.getMessage();
        }
    }


}
