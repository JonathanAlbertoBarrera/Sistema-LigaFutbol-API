package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.JugadorRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JugadorEstadisticaService {
    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private JugadorEstadisticaRepository jugadorEstadisticaRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Transactional
    public String registrarEstadisticas(Long idPartido, List<JugadorEstadisticaDTO> estadisticasDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        // Validar que el total de goles registrados no exceda los goles del partido
        int totalGolesReportados = estadisticasDTO.stream().mapToInt(JugadorEstadisticaDTO::getGoles).sum();
        if (totalGolesReportados > (partido.getGolesLocal() + partido.getGolesVisitante())) {
            throw new ValidationException("El total de goles registrados excede los goles del partido.");
        }

        for (JugadorEstadisticaDTO estadisticaDTO : estadisticasDTO) {
            Jugador jugador = jugadorRepository.findById(estadisticaDTO.getJugador().getId())
                    .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));

            // Validar si el jugador está expulsado
            if (jugador.isExpulsado()) {
                throw new ValidationException("El jugador " + jugador.getNombreCompleto() + " está expulsado y no puede registrar estadísticas en este partido.");
            }

            // Validar expulsión por doble amarilla o roja directa
            if (estadisticaDTO.getAmarillas() >= 2) {
                estadisticaDTO.setRojas(1);
                estadisticaDTO.setComentarioExpulsion("Expulsado por doble amarilla.");
            }

            // Registrar la estadística
            JugadorEstadistica estadistica = new JugadorEstadistica();
            estadistica.setPartido(partido);
            estadistica.setJugador(jugador);
            estadistica.setGoles(estadisticaDTO.getGoles());
            estadistica.setAmarillas(estadisticaDTO.getAmarillas());
            estadistica.setRojas(estadisticaDTO.getRojas());
            estadistica.setComentarioExpulsion(estadisticaDTO.getComentarioExpulsion());
            jugadorEstadisticaRepository.save(estadistica);

            // Si el jugador fue expulsado, marcarlo como expulsado
            if (estadisticaDTO.getRojas() > 0) {
                jugador.setExpulsado(true);
                jugadorRepository.save(jugador);
            }
        }

        actualizarExpulsiones(partido);

        return "Estadísticas de jugadores registradas correctamente.";
    }

    @Transactional
    public String modificarEstadistica(Long idEstadistica, JugadorEstadisticaDTO nuevaEstadisticaDTO) {
        JugadorEstadistica estadistica = jugadorEstadisticaRepository.findById(idEstadistica)
                .orElseThrow(() -> new NotFoundException("Estadística no encontrada"));

        Partido partido = estadistica.getPartido();
        Jugador jugador = estadistica.getJugador();

        // Validar que el total de goles registrados no exceda los goles del partido
        int totalGolesReportados = jugadorEstadisticaRepository.findByPartido(partido).stream()
                .mapToInt(JugadorEstadistica::getGoles).sum() - estadistica.getGoles() + nuevaEstadisticaDTO.getGoles();
        if (totalGolesReportados > (partido.getGolesLocal() + partido.getGolesVisitante())) {
            throw new ValidationException("El total de goles registrados excede los goles del partido.");
        }

        // Validar expulsión por doble amarilla o roja directa
        if (nuevaEstadisticaDTO.getAmarillas() >= 2) {
            nuevaEstadisticaDTO.setRojas(1);
            nuevaEstadisticaDTO.setComentarioExpulsion("Expulsado por doble amarilla.");
        }

        // Modificar la estadística
        estadistica.setGoles(nuevaEstadisticaDTO.getGoles());
        estadistica.setAmarillas(nuevaEstadisticaDTO.getAmarillas());
        estadistica.setRojas(nuevaEstadisticaDTO.getRojas());
        estadistica.setComentarioExpulsion(nuevaEstadisticaDTO.getComentarioExpulsion());
        jugadorEstadisticaRepository.save(estadistica);

        // Si el jugador fue expulsado en esta modificación, actualizar su estado
        if (nuevaEstadisticaDTO.getRojas() > 0) {
            jugador.setExpulsado(true);
        }

        actualizarExpulsiones(partido);
        jugadorRepository.save(jugador);

        return "Estadística del jugador modificada correctamente.";
    }

    private void actualizarExpulsiones(Partido partido) {
        List<Jugador> jugadoresExpulsados = jugadorRepository.findByExpulsadoTrue();
        for (Jugador jugador : jugadoresExpulsados) {
            long partidosCumplidos = jugadorEstadisticaRepository.countByJugadorAndPartido_FechaPartidoAfter(jugador, partido.getFechaPartido().minusWeeks(2));
            if (partidosCumplidos >= 2) {
                jugador.setExpulsado(false);
                jugadorRepository.save(jugador);
            }
        }
    }
}


