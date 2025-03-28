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
    public String registrarEstadisticas(Long idPartido, List<JugadorEstadisticaDTO> estadisticasLocalDTO,List<JugadorEstadisticaDTO> estadisticasVisitanteDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        // Validar que el total de goles de sus jugadores no supere a  los goles del eqipo
        int totalGolesReportadosLocal= estadisticasLocalDTO.stream().mapToInt(JugadorEstadisticaDTO::getGoles).sum()+partido.getAutogolesVisitante();
            if (totalGolesReportadosLocal > partido.getGolesLocal()) {
                throw new ValidationException("La suma de los goles de los jugadores del equipo local: "+partido.getEquipoLocal().getNombreEquipo()+" y autogoles del rival suman "+totalGolesReportadosLocal+ " goles lo cual excede los "+partido.getGolesLocal()+" goles que debe de tener.");
            }


        int totalGolesReportadosVisitante=estadisticasVisitanteDTO.stream().mapToInt(JugadorEstadisticaDTO::getGoles).sum()+partido.getAutogolesLocal();
            if (totalGolesReportadosVisitante > partido.getGolesVisitante()) {
                throw new ValidationException("La suma de los goles de los jugadores del equipo visitante: "+partido.getEquipoVisitante().getNombreEquipo()+" y autogoles del rival suman "+totalGolesReportadosVisitante+ " goles lo cual excede los "+partido.getGolesVisitante()+" goles que debe de tener.");
            }


        for (JugadorEstadisticaDTO estadisticaDTO : estadisticasLocalDTO) {
            Jugador jugador = jugadorRepository.findById(estadisticaDTO.getJugadorId())
                    .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));
            if(partido.getEquipoLocal().getId() != jugador.getEquipo().getId()){
                throw new ValidationException("El jugador "+jugador.getNombreCompleto()+ "con id "+jugador.getId()+" no pertenece al equipo: "+partido.getEquipoLocal().getNombreEquipo());
            }

            // Verificar si el jugador está expulsado
            if (jugador.isExpulsado()) {
                throw new ValidationException("El jugador " + jugador.getNombreCompleto() + " está expulsado y no puede registrar estadísticas en este partido.");
            }

            // Validar expulsión por doble amarilla o roja directa
            if (estadisticaDTO.getAmarillas() >= 2) {
                jugador.setExpulsado(true);
                jugadorRepository.save(jugador);
                estadisticaDTO.setComentarioExpulsion("Expulsado por doble amarilla.");
            }

            // Verificar si el jugador ha jugado al menos 3 partidos para poder registrar estadísticas en liguilla
            if (partido.getTorneo().isEsliguilla() && jugador.getPartidosJugados() < 3) {
                throw new ValidationException("El jugador "+jugador.getNombreCompleto()+ "no ha jugado al menos 3 partidos en jornada regular, por ello no es posible registrarle estadísticas en liguilla.");
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

        for (JugadorEstadisticaDTO estadisticaDTO : estadisticasVisitanteDTO) {
            Jugador jugador = jugadorRepository.findById(estadisticaDTO.getJugadorId())
                    .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));
            if(partido.getEquipoVisitante().getId() != jugador.getEquipo().getId()){
                throw new ValidationException("El jugador "+jugador.getNombreCompleto()+ "con id "+jugador.getId()+" no pertenece al equipo: "+partido.getEquipoVisitante().getNombreEquipo());
            }

            // Verificar si el jugador está expulsado
            if (jugador.isExpulsado()) {
                throw new ValidationException("El jugador " + jugador.getNombreCompleto() + " está expulsado y no puede registrar estadísticas en este partido.");
            }

            // Validar expulsión por doble amarilla o roja directa
            if (estadisticaDTO.getAmarillas() >= 2) {
                jugador.setExpulsado(true);
                jugadorRepository.save(jugador);
                estadisticaDTO.setComentarioExpulsion("Expulsado por doble amarilla.");
            }

            // Verificar si el jugador ha jugado al menos 3 partidos para poder registrar estadísticas en liguilla
            if (partido.getTorneo().isEsliguilla() && jugador.getPartidosJugados() < 3) {
                throw new ValidationException("El jugador "+jugador.getNombreCompleto()+ "no ha jugado al menos 3 partidos en jornada regular, por ello no es posible registrarle estadísticas en liguilla.");
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
            // Obtener la lista de los partidos donde el jugador fue expulsado, ordenados por fecha
            List<JugadorEstadistica> estadisticasJugador = jugadorEstadisticaRepository
                    .findByJugadorOrderByPartido_FechaPartidoDesc(jugador);

            if (!estadisticasJugador.isEmpty()) {
                // Tomamos la última expulsión
                JugadorEstadistica ultimaExpulsion = estadisticasJugador.get(0);

                // Filtrar los partidos del equipo del jugador después de la expulsión
                List<Partido> partidosDeEquipo = partidoRepository.findByEquipoAndFechaPartidoAfter(
                        jugador.getEquipo(), ultimaExpulsion.getPartido().getFechaPartido());

                // Verificar si el jugador ya jugó dos partidos después de su expulsión
                long partidosCumplidos = partidosDeEquipo.stream()
                        .filter(p -> p.getFechaPartido().isBefore(partido.getFechaPartido()))
                        .count();

                // Si el jugador ha jugado dos partidos posteriores, se le puede desmarcar como expulsado
                if (partidosCumplidos >= 2) {
                    jugador.setExpulsado(false);
                    jugadorRepository.save(jugador);
                }
            }
        }
    }


}





