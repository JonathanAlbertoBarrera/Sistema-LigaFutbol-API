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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            throw new ValidationException("Aún hay partidos de la fase regular no jugados. No se puede iniciar la liguilla.");
        }

        // Verificar si los partidos de liguilla ya han sido generados
        List<Partido> partidosLiguillaExistentes = partidoRepository.findByTorneoAndEsLiguillaTrue(torneo);
        if (!partidosLiguillaExistentes.isEmpty()) {
            throw new ValidationException("Los partidos de liguilla ya han sido generados para este torneo.");
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
            throw new ValidationException("Se necesitan al menos 2 equipos para iniciar la liguilla.");
        }

        // Generar partidos de liguilla
        generarPartidosLiguilla(torneo, equiposClasificados);

        return "Liguilla iniciada correctamente.";
    }

    private void generarPartidosLiguilla(Torneo torneo, List<Equipo> equiposClasificados) {
        // Obtener la fecha del último partido de la fase regular
        LocalDate fechaUltimoPartido = obtenerUltimaFechaPartidoFaseRegular(torneo);

        // Los partidos de liguilla deben comenzar una semana después del último partido de la fase regular
        LocalDate fechaPartido = fechaUltimoPartido.plusWeeks(1); // Una semana después

        List<Partido> partidosLiguilla = new ArrayList<>();

        // Generación de los partidos de ida y vuelta: Mejor vs Peor, 2do vs 3ro
        for (int i = 0; i < equiposClasificados.size() / 2; i++) {
            // Emparejar el primer equipo con el último (mejor vs peor)
            Equipo local = equiposClasificados.get(i); // Mejor equipo
            Equipo visitante = equiposClasificados.get(equiposClasificados.size() - 1 - i); // Peor equipo

            // Buscar cancha y árbitro disponibles
            LocalDate finalFechaPartido = fechaPartido;
            Optional<Cancha> canchaDisponible = canchaRepository.findByCampo(local.getCampo()).stream()
                    .filter(c -> c.isEstatusCancha()) // Solo canchas activas
                    .filter(c -> partidoRepository.findByCanchaAndFechaPartido(c, finalFechaPartido).isEmpty()) // Cancha libre
                    .findFirst();

            Optional<Arbitro> arbitroDisponible = arbitroRepository.findAll().stream()
                    .filter(a -> a.getUsuario().isEstatus()) // Solo árbitros activos
                    .filter(a -> partidoRepository.findByArbitroAndFechaPartido(a, finalFechaPartido).isEmpty()) // Sin partido asignado
                    .findFirst();

            if (canchaDisponible.isEmpty() || arbitroDisponible.isEmpty()) {
                throw new ValidationException("No hay recursos disponibles para crear los partidos.");
            }

            // Crear partido de ida
            Partido partidoIda = new Partido();
            partidoIda.setTorneo(torneo);
            partidoIda.setEquipoLocal(local);
            partidoIda.setEquipoVisitante(visitante);
            partidoIda.setCancha(canchaDisponible.get());
            partidoIda.setArbitro(arbitroDisponible.get());
            partidoIda.setFechaPartido(fechaPartido);  // Fecha del primer partido (partido de ida)
            partidoIda.setJugado(false);
            partidoIda.setFinal(false); // Aseguramos que no sea final aún
            partidoIda.setHora(LocalTime.of(8 + (i % 4) * 2, 0)); // Hora dinámica, alternando por partido
            partidosLiguilla.add(partidoIda);

            // Crear partido de vuelta (una semana después)
            Partido partidoVuelta = new Partido();
            partidoVuelta.setTorneo(torneo);
            partidoVuelta.setEquipoLocal(visitante);
            partidoVuelta.setEquipoVisitante(local);
            partidoVuelta.setCancha(canchaDisponible.get());
            partidoVuelta.setArbitro(arbitroDisponible.get());
            partidoVuelta.setFechaPartido(fechaPartido.plusWeeks(1));  // Fecha del partido de vuelta (una semana después)
            partidoVuelta.setJugado(false);
            partidoVuelta.setFinal(false); // Aseguramos que no sea final aún
            partidoVuelta.setHora(LocalTime.of(8 + (i % 4) * 2 + 1, 0)); // Hora dinámica, alternando por partido
            partidosLiguilla.add(partidoVuelta);

        }

        // Guardar partidos de liguilla
        partidoRepository.saveAll(partidosLiguilla);

        // Generar pagos para los partidos de liguilla
        partidosLiguilla.forEach(pagoService::generarPagoPorPartido);
    }

    private LocalDate obtenerUltimaFechaPartidoFaseRegular(Torneo torneo) {
        // Obtener el último partido jugado de la fase regular, asumiendo que ya se han jugado todos
        List<Partido> partidosFaseRegular = partidoRepository.findByTorneoAndJugadoTrue(torneo);
        return partidosFaseRegular.stream()
                .map(Partido::getFechaPartido)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new ValidationException("No se encontraron partidos jugados en la fase regular."));
    }


    public boolean faseTerminada(Torneo torneo) {
        // Verifica si todos los partidos de la fase han sido jugados
        List<Partido> partidosFase = partidoRepository.findByTorneo(torneo);
        return partidosFase.stream().allMatch(Partido::isJugado);
    }

    public void generarSiguienteFase(Torneo torneo) {
        // Verificar que la fase esté terminada
        if (faseTerminada(torneo)) {
            // Generar la siguiente fase
            generarPartidosLiguilla(torneo, obtenerEquiposFaseActual(torneo)); // Asegúrate de pasar la lista correcta
        } else {
            throw new ValidationException("Aún hay partidos pendientes en la fase actual.");
        }
    }

    public List<Equipo> obtenerEquiposFaseActual(Torneo torneo) {
        // Obtener los equipos clasificados a la fase actual
        return partidoRepository.findEquiposClasificados(torneo);
    }
}





