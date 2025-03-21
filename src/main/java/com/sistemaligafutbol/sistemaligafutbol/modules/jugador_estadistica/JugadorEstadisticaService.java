package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.JugadorRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
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
    @Autowired
    private TorneoRepository torneoRepository;

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

            // Verificar si el jugador está expulsado
            if (jugador.isExpulsado()) {
                throw new ValidationException("El jugador " + jugador.getNombreCompleto() + " está expulsado y no puede registrar estadísticas en este partido.");
            }

            // Validar expulsión por doble amarilla o roja directa
            if (estadisticaDTO.getAmarillas() >= 2) {
                estadisticaDTO.setRojas(1);
                estadisticaDTO.setComentarioExpulsion("Expulsado por doble amarilla.");
            }

            // Verificar si el jugador ha jugado al menos 3 partidos para poder registrar estadísticas en liguilla
            if (partido.getTorneo().isEsliguilla() && jugador.getPartidosJugados() < 3) {
                throw new ValidationException("El jugador no ha jugado suficientes partidos para registrar estadísticas en liguilla.");
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

            // Incrementar el contador de partidos jugados
            jugador.setPartidosJugados(jugador.getPartidosJugados() + 1);
            jugadorRepository.save(jugador);
        }

        actualizarExpulsiones(partido);

        return "Estadísticas de jugadores registradas correctamente.";
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





