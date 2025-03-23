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

    @Transactional
    public String iniciarLiguilla(Long idTorneo) {
        // Obtener el torneo
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        // Verificar que todos los partidos de la fase regular se hayan jugado
        List<Partido> partidosFaseRegular = partidoRepository.findByTorneo(torneo);
        if (partidosFaseRegular.stream().anyMatch(p -> !p.isJugado())) {
            throw new ValidationException("Aún hay partidos de la fase regular no jugados.");
        }

        // Verificar si los partidos de liguilla ya han sido generados
        List<Partido> partidosLiguillaExistentes = partidoRepository.findByTorneoAndEsLiguillaTrue(torneo);
        if (!partidosLiguillaExistentes.isEmpty()) {
            throw new ValidationException("Los partidos de liguilla ya han sido generados.");
        }

        // Marcar que el torneo está en liguilla
        torneo.setEsliguilla(true);
        torneoRepository.save(torneo);

        // Obtener los equipos clasificados a la liguilla basados en el número de equipos definido en el torneo
        List<Clasificacion> clasificaciones = clasificacionRepository.findClasificacionesByTorneo(torneo);

        // Limitar los equipos clasificados según el número especificado en el torneo
        List<Equipo> equiposClasificados = clasificaciones.stream()
                .limit(torneo.getEquiposLiguilla())
                .map(Clasificacion::getEquipo)
                .collect(Collectors.toList());

        if (equiposClasificados.size() < 2) {
            throw new ValidationException("Se necesitan al menos 2 equipos.");
        }

        // Marcar a los equipos clasificados como avanzados (avance = true)
        for (Clasificacion clasificacion : clasificaciones) {
            if (equiposClasificados.contains(clasificacion.getEquipo())) {
                clasificacion.setAvance(true);  // Marca como clasificado a la siguiente fase
                clasificacionRepository.save(clasificacion);
            } else {
                clasificacion.setAvance(false);  // Asegura que los no clasificados tengan avance = false
                clasificacionRepository.save(clasificacion);
            }
        }

        // Generar partidos de liguilla
        generarPartidosLiguilla(torneo, equiposClasificados);

        return "Liguilla iniciada correctamente.";
    }

    private void generarPartidosLiguilla(Torneo torneo, List<Equipo> equiposClasificados) {
        LocalDate fechaUltimoPartido = obtenerUltimaFechaPartidoFaseRegular(torneo);
        LocalDate fechaPartido = fechaUltimoPartido.plusWeeks(1);

        List<Partido> partidosLiguilla = new ArrayList<>();
        for (int i = 0; i < equiposClasificados.size() / 2; i++) {
            Equipo local = equiposClasificados.get(equiposClasificados.size() - 1 - i);
            Equipo visitante = equiposClasificados.get(i);

            LocalDate finalFechaPartido = fechaPartido;
            Optional<Cancha> canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream()
                    .filter(c -> c.isEstatusCancha())
                    .filter(c -> partidoRepository.findByCanchaAndFechaPartido(c, finalFechaPartido).isEmpty())
                    .findFirst();

            Optional<Arbitro> arbitroDisponible = arbitroRepository.findAll().stream()
                    .filter(a -> a.getUsuario().isEstatus())
                    .filter(a -> partidoRepository.findByArbitroAndFechaPartido(a, finalFechaPartido).isEmpty())
                    .findFirst();

            if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                throw new ValidationException("No hay recursos disponibles.");
            }

            Partido partidoIda = new Partido();
            partidoIda.setTorneo(torneo);
            partidoIda.setEquipoLocal(local);
            partidoIda.setEquipoVisitante(visitante);
            partidoIda.setCancha(canchaDisponible.get());
            partidoIda.setArbitro(arbitroDisponible.get());
            partidoIda.setFechaPartido(fechaPartido);
            partidoIda.setJugado(false);
            partidoIda.setFinal(false);
            partidoIda.setHora(LocalTime.of(8 + (i % 4) * 2, 0));
            partidoIda.setIdaVuelta("IDA");
            partidosLiguilla.add(partidoIda);


            Partido partidoVuelta = new Partido();
            partidoVuelta.setTorneo(torneo);
            partidoVuelta.setEquipoLocal(visitante);
            partidoVuelta.setEquipoVisitante(local);
            partidoVuelta.setCancha(canchaDisponible.get());
            partidoVuelta.setArbitro(arbitroDisponible.get());
            partidoVuelta.setFechaPartido(fechaPartido.plusWeeks(1));
            partidoVuelta.setJugado(false);
            partidoVuelta.setFinal(false);
            partidoVuelta.setHora(LocalTime.of(8 + (i % 4) * 2 + 1, 0));
            partidoVuelta.setIdaVuelta("VUELTA");
            partidosLiguilla.add(partidoVuelta);
        }

        partidoRepository.saveAll(partidosLiguilla);
        // Generar pagos para los partidos de liguilla
        partidosLiguilla.forEach(pagoService::generarPagoPorPartido);
    }

    private LocalDate obtenerUltimaFechaPartidoFaseRegular(Torneo torneo) {
        List<Partido> partidosFaseRegular = partidoRepository.findByTorneoAndJugadoTrue(torneo);
        return partidosFaseRegular.stream()
                .map(Partido::getFechaPartido)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new ValidationException("No se encontraron partidos jugados en la fase regular."));
    }

    // Verificar si la fase está terminada
    public boolean faseTerminada(Torneo torneo) {
        List<Partido> partidosFase = partidoRepository.findByTorneo(torneo);
        return partidosFase.stream().allMatch(Partido::isJugado);
    }

    // Generar la siguiente fase según los equipos ganadores
    public void generarSiguienteFase(Torneo torneo) {
        if (faseTerminada(torneo)) {
            // Obtener los ganadores de la fase anterior
            List<Equipo> equiposGanadores = obtenerEquiposFaseActual(torneo);

            // Generar la siguiente fase de acuerdo al número de equipos clasificados
            if (equiposGanadores.size() != 2) {
                if(equiposGanadores.size()==1){
                    throw new ValidationException("El torneo ya tiene un ganador");
                }
                if(equiposGanadores.size() %2 !=0){
                    throw new ValidationException("Debe haber un número par de equipos que pasen a la siguiente ronda para crear la siguiente");
                }
                generarPartidosLiguilla(torneo, equiposGanadores); // Generamos siguiente fase los equipos ganadores
            } else {
                // Si solo quedan 2 equipos, es la final
                generarFinal(torneo, equiposGanadores); // Generamos la final con los 2 equipos ganadores
            }
        } else {
            throw new ValidationException("Aún hay partidos pendientes en la fase actual.");
        }
    }

    public List<Equipo> obtenerEquiposFaseActual(Torneo torneo) {
        // Obtener los equipos con avance = true (es decir, los que han pasado a la siguiente fase)
        return clasificacionRepository.findClasificacionesByTorneo(torneo).stream()
                .filter(Clasificacion::isAvance)  // Solo los equipos con avance = true
                .map(Clasificacion::getEquipo)  // Obtener los equipos de las clasificaciones
                .collect(Collectors.toList());
    }

    private void generarFinal(Torneo torneo, List<Equipo> equiposGanadores) {
        if(equiposGanadores.size()!=2){
            throw new ValidationException("Debe haber exactamente 2 equipos para generar la final");
        }

        LocalDate fechaUltimoPartido = obtenerUltimaFechaPartidoFaseRegular(torneo);
        LocalDate fechaPartido = fechaUltimoPartido.plusWeeks(1);

        List<Partido> finales = new ArrayList<>();
        for (int i = 0; i < equiposGanadores.size() / 2; i++) {
            Equipo local = equiposGanadores.get(equiposGanadores.size() - 1 - i);
            Equipo visitante = equiposGanadores.get(i);

            LocalDate finalFechaPartido = fechaPartido;
            Optional<Cancha> canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream()
                    .filter(c -> c.isEstatusCancha())
                    .filter(c -> partidoRepository.findByCanchaAndFechaPartido(c, finalFechaPartido).isEmpty())
                    .findFirst();

            Optional<Arbitro> arbitroDisponible = arbitroRepository.findAll().stream()
                    .filter(a -> a.getUsuario().isEstatus())
                    .filter(a -> partidoRepository.findByArbitroAndFechaPartido(a, finalFechaPartido).isEmpty())
                    .findFirst();

            if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                throw new ValidationException("No hay recursos disponibles.");
            }

            Partido finalIda = new Partido();
            finalIda.setTorneo(torneo);
            finalIda.setEquipoLocal(local);
            finalIda.setEquipoVisitante(visitante);
            finalIda.setCancha(canchaDisponible.get());
            finalIda.setArbitro(arbitroDisponible.get());
            finalIda.setFechaPartido(fechaPartido);
            finalIda.setJugado(false);
            finalIda.setFinal(true);
            finalIda.setHora(LocalTime.of(8 + (i % 4) * 2, 0));
            finalIda.setIdaVuelta("IDA");
            finales.add(finalIda);

            Partido finalVuelta = new Partido();
            finalVuelta.setTorneo(torneo);
            finalVuelta.setEquipoLocal(visitante);
            finalVuelta.setEquipoVisitante(local);
            finalVuelta.setCancha(canchaDisponible.get());
            finalVuelta.setArbitro(arbitroDisponible.get());
            finalVuelta.setFechaPartido(fechaPartido.plusWeeks(1));
            finalVuelta.setJugado(false);
            finalVuelta.setFinal(true);
            finalVuelta.setHora(LocalTime.of(8 + (i % 4) * 2 + 1, 0));
            finalVuelta.setIdaVuelta("VUELTA");
            finales.add(finalVuelta);
            Torneo torneoa=torneoRepository.findById(torneo.getId())
                    .orElseThrow(()->new NotFoundException("Torneo no encontrado"));
            torneoa.setFechaFin(fechaPartido.plusWeeks(1));
            torneoRepository.save(torneoa);
        }

        partidoRepository.saveAll(finales);
        // Generar pagos para los partidos de liguilla
        finales.forEach(pagoService::generarPagoPorPartido);
    }

}








