package com.sistemaligafutbol.sistemaligafutbol.modules.partido.services;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.CanchaRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion.ClasificacionRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.PagoService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.ArbitroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartidosLiguillaService {
    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private PagoService pagoService;

    //-----GENERAR PARTIDOS DE LIGUILLA
    @Transactional
    public String iniciarLiguilla(Long idTorneo) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        if (!torneo.isIniciado()) {
            throw new ValidationException("El torneo aún no ha iniciado. No se puede generar la liguilla.");
        }

        if (torneo.isEsliguilla()) {
            throw new ValidationException("La liguilla ya ha sido generada previamente para este torneo.");
        }

        if (torneo.getGanador() != null) {
            throw new ValidationException("Este torneo ya tiene un ganador. No se puede reiniciar la liguilla.");
        }

        long partidosPendientes = partidoRepository.countByTorneoAndJugadoFalse(torneo);
        if (partidosPendientes > 0) {
            throw new ValidationException("No se puede generar la liguilla porque aún hay " + partidosPendientes + " partidos pendientes en la fase regular.");
        }

        List<Equipo> equiposClasificados = clasificacionRepository.findTopEquiposByClasificacion(torneo)
                .stream()
                .limit(torneo.getEquiposLiguilla())
                .collect(Collectors.toList());

        if (equiposClasificados.size() < torneo.getEquiposLiguilla()) {
            throw new ValidationException("No hay suficientes equipos para la liguilla. Se requieren " + torneo.getEquiposLiguilla() + " pero solo hay " + equiposClasificados.size() + ".");
        }

        generarPartidosLiguilla(torneo, equiposClasificados);
        torneo.setEsliguilla(true);
        torneoRepository.save(torneo);

        return "Liguilla generada correctamente.";
    }

    private void generarPartidosLiguilla(Torneo torneo, List<Equipo> equiposClasificados) {
        int totalFases = (int) (Math.log(equiposClasificados.size()) / Math.log(2));
        LocalDate fechaPartido = torneo.getFechaFin().minusWeeks(totalFases);

        List<Partido> partidosLiguilla = new ArrayList<>();

        for (int fase = 0; fase < totalFases; fase++) {
            List<Equipo> equiposFase = new ArrayList<>(equiposClasificados);
            equiposFase.sort(Comparator.comparingInt(equipo -> getPosicionEquipo(torneo, equipo)));

            for (int i = 0; i < equiposFase.size() / 2; i++) {
                Equipo local = equiposFase.get(i);
                Equipo visitante = equiposFase.get(equiposFase.size() - 1 - i);

                Optional<Cancha> canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream().findFirst();
                Optional<Arbitro> arbitroDisponible = arbitroRepository.findAll().stream().findFirst();

                if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                    throw new ValidationException("No hay suficientes recursos para generar la siguiente fase.");
                }

                Partido partidoIda = new Partido();
                partidoIda.setTorneo(torneo);
                partidoIda.setEquipoLocal(local);
                partidoIda.setEquipoVisitante(visitante);
                partidoIda.setCancha(canchaDisponible.get());
                partidoIda.setArbitro(arbitroDisponible.get());
                partidoIda.setFechaPartido(fechaPartido);
                partidoIda.setHora(LocalTime.of(10 + i * 2, 0));
                partidoIda.setJugado(false);
                partidosLiguilla.add(partidoIda);
                pagoService.generarPagoPorPartido(partidoIda);

                Partido partidoVuelta = new Partido();
                partidoVuelta.setTorneo(torneo);
                partidoVuelta.setEquipoLocal(visitante);
                partidoVuelta.setEquipoVisitante(local);
                partidoVuelta.setCancha(canchaDisponible.get());
                partidoVuelta.setArbitro(arbitroDisponible.get());
                partidoVuelta.setFechaPartido(fechaPartido.plusWeeks(1));
                partidoVuelta.setHora(LocalTime.of(10 + i * 2, 0));
                partidoVuelta.setJugado(false);
                partidosLiguilla.add(partidoVuelta);
                pagoService.generarPagoPorPartido(partidoVuelta);
            }

            partidosLiguilla.forEach(partidoRepository::save);
            fechaPartido = fechaPartido.plusWeeks(2);
        }
    }

    public boolean faseTerminada(Torneo torneo, List<Equipo> equiposFase) {
        List<Partido> partidosFase = partidoRepository.findByTorneoAndEquiposIn(torneo, equiposFase);

        long partidosEsperados = equiposFase.size(); // Mitad de equipos = cantidad de enfrentamientos
        long partidosJugados = partidosFase.stream().filter(Partido::isJugado).count();

        if (partidosJugados != partidosEsperados * 2) {
            return false;  //  Aún no se han jugado todos los partidos de la fase
        }

        // Validar que cada llave tenga un ganador
        for (Partido partido : partidosFase) {
            Optional<Partido> partidoIda = partidoRepository.findByEquipoLocalAndEquipoVisitanteAndFechaPartido(
                    partido.getEquipoVisitante(), partido.getEquipoLocal(), partido.getFechaPartido().minusWeeks(1));

            if (partidoIda.isPresent()) {
                Partido ida = partidoIda.get();
                int totalGolesLocal = ida.getGolesVisitante() + partido.getGolesLocal();
                int totalGolesVisitante = ida.getGolesLocal() + partido.getGolesVisitante();

                // Si hay empate global y no se ha definido un ganador en penales o tiempos extra, detener avance
                if (totalGolesLocal == totalGolesVisitante &&
                        (partido.getDescripcionResultado() == null || partido.getDescripcionResultado().isEmpty())) {
                    return false;
                }
            }
        }

        return true;
    }

    private int getPosicionEquipo(Torneo torneo, Equipo equipo) {
        List<Equipo> equiposOrdenados = clasificacionRepository.findTopEquiposByClasificacion(torneo);
        return equiposOrdenados.indexOf(equipo) + 1; // +1 para que empiece desde 1
    }

    public List<Equipo> obtenerEquiposFaseActual(Torneo torneo) {
        List<Partido> partidosFase = partidoRepository.findByTorneoAndJugadoFalse(torneo);
        Set<Equipo> equipos = new HashSet<>();

        for (Partido partido : partidosFase) {
            equipos.add(partido.getEquipoLocal());
            equipos.add(partido.getEquipoVisitante());
        }

        return new ArrayList<>(equipos);
    }

    public void generarSiguienteFase(Torneo torneo) {
        List<Equipo> equiposGanadores = obtenerEquiposGanadoresFaseAnterior(torneo);
        List<Partido> nuevosPartidos = new ArrayList<>();

        if (equiposGanadores.size() == 2) {
            torneo.setGanador(equiposGanadores.get(0));
            torneo.setEstatusTorneo(false);
            torneo.setEsliguilla(false);
            torneo.setFechaFin(LocalDate.now());
            torneoRepository.save(torneo);
            return;
        }

        equiposGanadores.sort(Comparator.comparingInt(equipo -> getPosicionEquipo(torneo, equipo)));
        LocalDate fechaSiguienteFase = torneo.getFechaFin().minusWeeks((int) (Math.log(equiposGanadores.size()) / Math.log(2)));

        for (int i = 0; i < equiposGanadores.size() / 2; i++) {
            Equipo local = equiposGanadores.get(i);
            Equipo visitante = equiposGanadores.get(equiposGanadores.size() - 1 - i);

            Optional<Cancha> canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream().findFirst();
            Optional<Arbitro> arbitroDisponible = arbitroRepository.findAll().stream().findFirst();

            if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                throw new ValidationException("No hay suficientes recursos para generar la siguiente fase.");
            }

            Partido partidoIda = new Partido();
            partidoIda.setTorneo(torneo);
            partidoIda.setEquipoLocal(local);
            partidoIda.setEquipoVisitante(visitante);
            partidoIda.setFechaPartido(fechaSiguienteFase);
            partidoIda.setJugado(false);
            nuevosPartidos.add(partidoIda);
            pagoService.generarPagoPorPartido(partidoIda);

            partidoRepository.saveAll(nuevosPartidos);
        }
    }

    private List<Equipo> obtenerEquiposGanadoresFaseAnterior(Torneo torneo) {
        List<Equipo> equiposGanadores = new ArrayList<>();

        // Obtener los partidos de la última fase jugada
        List<Partido> partidosFaseAnterior = partidoRepository.findUltimosPartidosFase(torneo);

        for (Partido partido : partidosFaseAnterior) {
            Optional<Partido> partidoIda = partidoRepository.findByEquipoLocalAndEquipoVisitanteAndFechaPartido(
                    partido.getEquipoVisitante(), partido.getEquipoLocal(), partido.getFechaPartido().minusWeeks(1));

            if (partidoIda.isPresent()) {
                Partido ida = partidoIda.get();
                int totalGolesLocal = ida.getGolesVisitante() + partido.getGolesLocal();
                int totalGolesVisitante = ida.getGolesLocal() + partido.getGolesVisitante();

                Equipo equipoGanador;
                if (totalGolesLocal > totalGolesVisitante) {
                    equipoGanador = partido.getEquipoLocal();
                } else if (totalGolesVisitante > totalGolesLocal) {
                    equipoGanador = partido.getEquipoVisitante();
                } else {
                    if (partido.getDescripcionResultado() == null || partido.getDescripcionResultado().isEmpty()) {
                        throw new ValidationException("Empate en la serie. Se debe indicar un ganador en tiempo extra o penales.");
                    }

                    equipoGanador = partido.getDescripcionResultado().contains(partido.getEquipoLocal().getNombreEquipo()) ?
                            partido.getEquipoLocal() : partido.getEquipoVisitante();
                }

                equiposGanadores.add(equipoGanador);
            }
        }

        return equiposGanadores;
    }

}
