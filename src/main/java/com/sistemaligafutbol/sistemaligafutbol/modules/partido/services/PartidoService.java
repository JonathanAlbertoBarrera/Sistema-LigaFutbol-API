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
import java.util.stream.Collectors;

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

        partidoRepository.save(partido);

        // Gestionar clasificación solo en fase regular
        if (!partido.getTorneo().isEsliguilla()) {
            Clasificacion clasificacionLocal = clasificacionRepository.findByEquipo(partido.getEquipoLocal())
                    .orElseThrow(() -> new NotFoundException("Clasificación de equipo no encontrada"));
            Clasificacion clasificacionVisitante = clasificacionRepository.findByEquipo(partido.getEquipoVisitante())
                    .orElseThrow(() -> new NotFoundException("Clasificación de equipo no encontrada"));

            clasificacionLocal.setGolesAFavor(clasificacionLocal.getGolesAFavor() + resultadoDTO.getGolesLocal());
            clasificacionVisitante.setGolesAFavor(clasificacionVisitante.getGolesAFavor() + resultadoDTO.getGolesVisitante());
            clasificacionLocal.setGolesEnContra(clasificacionLocal.getGolesEnContra() + resultadoDTO.getGolesVisitante());
            clasificacionVisitante.setGolesEnContra(clasificacionVisitante.getGolesEnContra() + resultadoDTO.getGolesLocal());

            if (resultadoDTO.getGolesLocal() == resultadoDTO.getGolesVisitante()) {
                clasificacionLocal.setPartidosEmpatados(clasificacionLocal.getPartidosEmpatados() + 1);
                clasificacionVisitante.setPartidosEmpatados(clasificacionVisitante.getPartidosEmpatados() + 1);
                clasificacionLocal.setPuntos(clasificacionLocal.getPuntos() + 1);
                clasificacionVisitante.setPuntos(clasificacionVisitante.getPuntos() + 1);
            }

            if (resultadoDTO.getGolesLocal() > resultadoDTO.getGolesVisitante()) {
                clasificacionLocal.setPartidosGanados(clasificacionLocal.getPartidosGanados() + 1);
                clasificacionVisitante.setPartidosPerdidos(clasificacionVisitante.getPartidosPerdidos() + 1);
                clasificacionLocal.setPuntos(clasificacionLocal.getPuntos() + 3);
            }

            if (resultadoDTO.getGolesLocal() < resultadoDTO.getGolesVisitante()) {
                clasificacionLocal.setPartidosPerdidos(clasificacionLocal.getPartidosPerdidos() + 1);
                clasificacionVisitante.setPartidosGanados(clasificacionVisitante.getPartidosGanados() + 1);
                clasificacionVisitante.setPuntos(clasificacionVisitante.getPuntos() + 3);
            }

            clasificacionRepository.save(clasificacionLocal);
            clasificacionRepository.save(clasificacionVisitante);
        }

        if (partido.getTorneo().isEsliguilla()) {
            registrarResultadoLiguilla(partido.getId(), resultadoDTO);
        }

        if (partidosLiguillaService.faseTerminada(partido.getTorneo()) && !esFinalDeLiguilla(partido)) {
            partidosLiguillaService.iniciarLiguilla(partido.getTorneo().getId());
        }

        return "Resultado registrado correctamente.";
    }

    @Transactional
    public String registrarResultadoLiguilla(Long idPartido, PartidoResultadoDTO resultadoDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        partido.setGolesLocal(resultadoDTO.getGolesLocal());
        partido.setGolesVisitante(resultadoDTO.getGolesVisitante());
        partido.setJugado(true);
        if(resultadoDTO.getTipoDesempate()!=null && !resultadoDTO.equals("")){
            partido.setTipoDesempate(resultadoDTO.getTipoDesempate());
        }

        partidoRepository.save(partido);

        if(partido.getIdaVuelta().equals("VUELTA")){
            if(resultadoDTO.getTipoDesempate()==null || resultadoDTO.equals("")){
                throw new ValidationException("Es un partido de vuelta de liguilla. Debes indicar el tipo de desempate (NORMAL,TIEMPO_EXTRA,PENALES) ");
            }
            if(!resultadoDTO.getTipoDesempate().equals("NORMAL") && !resultadoDTO.getTipoDesempate().equals("TIEMPO_EXTRA") && !resultadoDTO.getTipoDesempate().equals("PENALES")){
                System.out.println("Dato: "+resultadoDTO.getTipoDesempate());
                throw new ValidationException("Tipo de desempate desconocido. Intenta con estas opciones: (NORMAL, TIEMPO_EXTRA, PENALES) ");
            }
            //PARA DECIDIR QUIEN PASO
            actualizarAvanceEquipo(partido, resultadoDTO);
        }

        // Si el partido es final, asignamos ganador y finalizamos el torneo
        if (esFinalDeLiguilla(partido) & partido.getIdaVuelta().equals("VUELTA")) {
            asignarGanadorYFinalizarTorneo(partido.getTorneo());
            resetearPartidosJugadosAlTerminarTorneo(partido.getTorneo());
        }

        // Verificar si la fase está terminada y generar la siguiente fase
        if (partidosLiguillaService.faseTerminada(partido.getTorneo())) {
            if (existeUnSoloGanador(partido.getTorneo())) {
                // No generar la siguiente fase si ya hay un solo equipo ganador
                return "Torneo finalizado, el ganador es: "+partido.getTorneo().getGanador().getNombreEquipo();
            }
            partidosLiguillaService.generarSiguienteFase(partido.getTorneo());
        }

        return "Resultado registrado correctamente.";
    }

    private void actualizarAvanceEquipo(Partido partido, PartidoResultadoDTO resultadoDTO) {
        // Buscar el partido de ida relacionado con este partido de vuelta
        Partido partidoIda = partidoRepository.findByTorneoAndEquipoLocalAndEquipoVisitanteAndIdaVuelta(partido.getTorneo(), partido.getEquipoVisitante(), partido.getEquipoLocal(), "IDA")
                .orElseThrow(() -> new NotFoundException("No se encontró el partido de ida para este enfrentamiento"));
        Partido partidoVuelta=partidoRepository.findById(partido.getId())
                .orElseThrow(()->new NotFoundException("Partido no encontrado"));

        // Sumar los goles del partido de ida y el partido de vuelta
        int golesLocalTotal = partidoIda.getGolesVisitante() + partido.getGolesLocal();
        int golesVisitanteTotal = partidoIda.getGolesLocal() + partido.getGolesVisitante();

        // Si el tipo de desempate fue NORMAL
        Clasificacion clasificacionLocal=clasificacionRepository.findByEquipo(partido.getEquipoLocal())
                .orElseThrow(()->new NotFoundException("Clasificación del equipo local no encontrada"));
        Clasificacion clasificacionVisitante=clasificacionRepository.findByEquipo(partido.getEquipoVisitante())
                .orElseThrow(()->new NotFoundException("Clasificación del equipo visitante no encontrada"));

        if ("NORMAL".equals(partido.getTipoDesempate())) {
            if (golesLocalTotal > golesVisitanteTotal) {
                clasificacionLocal.setAvance(true);
                clasificacionVisitante.setAvance(false);
            } else if (golesVisitanteTotal > golesLocalTotal) {
                clasificacionVisitante.setAvance(true);
                clasificacionLocal.setAvance(false);
            } else {
                // En caso de empate, se decidirá por tiempo extra o penales
                throw new ValidationException("El marcador está empatado. Se requiere tiempo extra o penales.");
            }
        }

        // Si el tipo de desempate fue TIEMPO EXTRA
        else if ("TIEMPO_EXTRA".equals(partido.getTipoDesempate())) {
            if (golesLocalTotal > golesVisitanteTotal) {
                clasificacionLocal.setAvance(true);
                clasificacionVisitante.setAvance(false);
            } else if (golesVisitanteTotal > golesLocalTotal) {
                clasificacionVisitante.setAvance(true);
                clasificacionLocal.setAvance(false);
            }else{
                throw new ValidationException("El marcador global está empatado. Se requiere decidir el partido en penales");
            }
        }

        // Si el tipo de desempate fue PENALES
        else if ("PENALES".equals(partido.getTipoDesempate())) {
            if(resultadoDTO.getGolesLocalPenales()==null || resultadoDTO.getGolesVisitantePenales()==null){
                throw new ValidationException("Debes ingresar los penales que anotó tanto el equipo local como el equipo visitante");
            }
            if(golesLocalTotal != golesVisitanteTotal){
                throw new ValidationException("El marcador global no está empatado por lo que no se puede decidir el partido por penales");
            }
            partidoVuelta.setGolesLocalPenales(resultadoDTO.getGolesLocalPenales());
            partidoVuelta.setGolesVisitante(resultadoDTO.getGolesVisitantePenales());
            partidoRepository.save(partidoVuelta);
            int golesLocalPenales = resultadoDTO.getGolesLocalPenales();
            int golesVisitantePenales = resultadoDTO.getGolesVisitantePenales();

            if (golesLocalPenales > golesVisitantePenales) {
                clasificacionLocal.setAvance(true);
                clasificacionVisitante.setAvance(false);
            } else if (golesVisitantePenales > golesLocalPenales) {
                clasificacionVisitante.setAvance(true);
                clasificacionLocal.setAvance(false);
            } else {
                // En caso de empate en los penales, se debería de implementar una lógica adicional o declarar empate
                throw new ValidationException("El marcador de penales está empatado. Alguien debe de ganar, así que por favor registra correctamente el resultado de los penales (Algún equipo debe anotar al menos un penal más que el otro equipo)");
            }
        }

        clasificacionRepository.save(clasificacionLocal);
        clasificacionRepository.save(clasificacionVisitante);
    }

    private boolean esFinalDeLiguilla(Partido partido) {
        return partido.isFinal();
    }

    public boolean existeUnSoloGanador(Torneo torneo) {
        // Verificar si solo hay un equipo con el atributo 'avance' en true
        return clasificacionRepository.findClasificacionesByTorneo(torneo).stream()
                .filter(c -> c.isAvance())
                .count() == 1;
    }

    private void asignarGanadorYFinalizarTorneo(Torneo torneo) {
        // Buscar el equipo con avance true en la tabla de clasificación
        List<Clasificacion> clasificaciones = clasificacionRepository.findClasificacionesByTorneo(torneo);

        // Filtrar los equipos que tienen "avance" en true
        List<Clasificacion> equiposConAvance = clasificaciones.stream()
                .filter(Clasificacion::isAvance)
                .collect(Collectors.toList());

        // Verificar que solo haya un equipo con "avance" en true
        if (equiposConAvance.size() == 1) {
            // El equipo que tiene "avance" en true es el ganador
            Equipo ganador = equiposConAvance.get(0).getEquipo();

            // Asignar al ganador en el torneo
            torneo.setGanador(ganador);

            // Cambiar el estatus del torneo a finalizado
            torneo.setEstatusTorneo(false); // Establece el estatus del torneo como terminado
            torneo.setEsliguilla(false); // Establece que la liguilla ya ha terminado

            torneoRepository.save(torneo);
        } else {
            // Si no hay un solo equipo con "avance" en true, no podemos asignar un ganador
            throw new ValidationException("Solo puede haber un ganador del torneo");
        }
    }

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













