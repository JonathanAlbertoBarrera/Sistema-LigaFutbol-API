package com.sistemaligafutbol.sistemaligafutbol.modules.partido.services;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion.Clasificacion;
import com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion.ClasificacionRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.JugadorRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica.JugadorEstadisticaService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoResultadoDTO;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartidoService {
    @Autowired
    private PartidoRepository partidoRepository;
    @Autowired
    private PartidosLiguillaService partidosLiguillaService;
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private TorneoRepository torneoRepository;
    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Transactional
    public String registrarResultado(Long idPartido, PartidoResultadoDTO resultadoDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        if(partido.isJugado()){
            throw new ValidationException("Ya se le asignó un resultado al partido");
        }

        // Validar goles
        if (resultadoDTO.getGolesLocal() < 0 || resultadoDTO.getGolesVisitante() < 0) {
            throw new ValidationException("Los goles no pueden ser negativos.");
        }

        // Asignar goles de los equipos
        partido.setGolesLocal(resultadoDTO.getGolesLocal());
        partido.setGolesVisitante(resultadoDTO.getGolesVisitante());
        partido.setJugado(true);

        // Guardar el resultado del partido
        partido.setDescripcionResultado(resultadoDTO.getDescripcionResultado());
        if ("PENALES".equals(partido.getTipoDesempate())) {
            partido.setGolesLocalPenales(resultadoDTO.getGolesLocalPenales());
            partido.setGolesVisitantePenales(resultadoDTO.getGolesVisitantePenales());
        }

        partidoRepository.save(partido);

        // Si el partido es de liguilla, registrar resultado de liguilla
        if (partido.getTorneo().isEsliguilla()) {
            registrarResultadoLiguilla(partido.getId(), resultadoDTO);
        }

        // Verificar si la fase está terminada y generar la siguiente fase
        if (partidosLiguillaService.faseTerminada(partido.getTorneo())) {
            partidosLiguillaService.iniciarLiguilla(partido.getTorneo().getId());
        }

        // Verificar si se ha llegado a la final de la liguilla y asignar al ganador
        if (esFinalDeLiguilla(partido)) {
            asignarGanadorYFinalizarTorneo(partido.getTorneo());

            // Resetear los partidos jugados de los jugadores después de la final
            resetearPartidosJugadosAlTerminarTorneo(partido.getTorneo());
        }

        Clasificacion clasificacionLocal =clasificacionRepository.findByEquipo(partido.getEquipoLocal())
                .orElseThrow(()-> new NotFoundException("Clasificación de equipo no encontrada"));
        Clasificacion clasificacionVisitante =clasificacionRepository.findByEquipo(partido.getEquipoVisitante())
                .orElseThrow(()-> new NotFoundException("Clasificación de equipo no encontrada"));
        //ASIGNAR DATOS PARA LA TABLA DE CLASIFICACION
        if(!partido.getTorneo().isEsliguilla()){
            clasificacionLocal.setGolesAFavor(clasificacionLocal.getGolesAFavor()+resultadoDTO.getGolesLocal());//sumar goles a favor al local
            clasificacionVisitante.setGolesAFavor(clasificacionVisitante.getGolesAFavor()+resultadoDTO.getGolesVisitante());//sumar goles al visitante

            clasificacionLocal.setGolesEnContra(clasificacionLocal.getGolesEnContra()+resultadoDTO.getGolesVisitante()); //sumar goles en contra al local
            clasificacionVisitante.setGolesEnContra(clasificacionVisitante.getGolesEnContra()+resultadoDTO.getGolesLocal()); //sumar goles en contra al visitante
            if(resultadoDTO.getGolesLocal()==resultadoDTO.getGolesVisitante()){
                //ambos se les agrega partido empatado
                clasificacionLocal.setPartidosEmpatados(clasificacionLocal.getPartidosEmpatados()+1);
                clasificacionVisitante.setPartidosEmpatados(clasificacionVisitante.getPartidosEmpatados()+1);

                //se les suma un punto
                clasificacionLocal.setPuntos(clasificacionLocal.getPuntos()+1);
                clasificacionVisitante.setPuntos(clasificacionVisitante.getPuntos()+1);
            }

            if (resultadoDTO.getGolesLocal()>resultadoDTO.getGolesVisitante()){
                //se le agrega un partido ganado al local y uno perdido al visitante
                clasificacionLocal.setPartidosGanados(clasificacionLocal.getPartidosGanados()+1);
                clasificacionVisitante.setPartidosPerdidos(clasificacionVisitante.getPartidosPerdidos()+1);

                //se le suma 3 puntos al local
                clasificacionLocal.setPuntos(clasificacionLocal.getPuntos()+3);
            }

            if (resultadoDTO.getGolesLocal()<resultadoDTO.getGolesVisitante()){
                //se le agrega un partido ganado al visitante y uno perdido al local
                clasificacionLocal.setPartidosPerdidos(clasificacionLocal.getPartidosPerdidos()+1);
                clasificacionVisitante.setPartidosGanados(clasificacionVisitante.getPartidosGanados()+1);

                //se le suma 3 puntos al visitante
                clasificacionLocal.setPuntos(clasificacionVisitante.getPuntos()+3);
            }
        }

        return "Resultado registrado correctamente.";
    }

    // Método para registrar resultado de liguilla
    @Transactional
    public String registrarResultadoLiguilla(Long idPartido, PartidoResultadoDTO resultadoDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        // Asignar goles de ida y vuelta
        partido.setGolesLocal(resultadoDTO.getGolesLocal());
        partido.setGolesVisitante(resultadoDTO.getGolesVisitante());
        partido.setJugado(true);

        // Verificar si hubo tiempo extra o penales
        if ("PENALES".equals(partido.getTipoDesempate())) {
            partido.setGolesLocalPenales(resultadoDTO.getGolesLocalPenales());
            partido.setGolesVisitantePenales(resultadoDTO.getGolesVisitantePenales());
        }

        partidoRepository.save(partido);

        // Si la fase está terminada, generar la siguiente fase
        if (partidosLiguillaService.faseTerminada(partido.getTorneo())) {
            partidosLiguillaService.generarSiguienteFase(partido.getTorneo());
        }

        return "Resultado registrado correctamente.";
    }

    // Método para verificar si es la final de la liguilla
    private boolean esFinalDeLiguilla(Partido partido) {
        return partido.isFinal();  // Verificamos el campo 'isFinal' del partido
    }

    // Asignar ganador y finalizar el torneo después de la final
    public void asignarGanadorYFinalizarTorneo(Torneo torneo) {
        // Obtener el partido final
        Partido finalPartido = partidoRepository.findFinalByTorneo(torneo);

        if (finalPartido != null && finalPartido.isJugado()) {
            // Comprobar si el marcador global está empatado
            if (finalPartido.getGolesLocal() == finalPartido.getGolesVisitante()) {
                // Si el marcador global está empatado, se decide por penales
                if (finalPartido.getGolesLocalPenales() > finalPartido.getGolesVisitantePenales()) {
                    torneo.setGanador(finalPartido.getEquipoLocal());
                } else if (finalPartido.getGolesVisitantePenales() > finalPartido.getGolesLocalPenales()) {
                    torneo.setGanador(finalPartido.getEquipoVisitante());
                } else {
                    // Si el empate persiste en los penales, implementar lógica adicional si es necesario
                    throw new ValidationException("El empate persiste en los penales. Implementa lógica adicional si es necesario.");
                }
            } else {
                // Si no hay empate global, asignamos al ganador por el marcador global
                if (finalPartido.getGolesLocal() > finalPartido.getGolesVisitante()) {
                    torneo.setGanador(finalPartido.getEquipoLocal());
                } else {
                    torneo.setGanador(finalPartido.getEquipoVisitante());
                }
            }

            // Cambiar el estatus del torneo a finalizado
            torneo.setEstatusTorneo(false); // Establece el estatus del torneo como terminado
            torneo.setEsliguilla(false); // Establece que la liguilla ya ha terminado

            // Guardar cambios en el torneo
            torneoRepository.save(torneo);
        }
    }

    // Método para resetear los partidos jugados de los jugadores al terminar el torneo
    public void resetearPartidosJugadosAlTerminarTorneo(Torneo torneo) {
        // Obtener todos los jugadores que participaron en el torneo
        List<Jugador> jugadoresDelTorneo = jugadorRepository.findJugadoresPorTorneo(torneo);

        // Resetear el número de partidos jugados de cada jugador
        for (Jugador jugador : jugadoresDelTorneo) {
            jugador.setPartidosJugados(0);
            jugadorRepository.save(jugador);
        }
    }
}












