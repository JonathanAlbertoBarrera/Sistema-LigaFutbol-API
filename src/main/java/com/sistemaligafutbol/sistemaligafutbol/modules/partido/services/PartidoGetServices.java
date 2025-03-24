package com.sistemaligafutbol.sistemaligafutbol.modules.partido.services;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.ArbitroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PartidoGetServices {

    @Autowired
    private PartidoRepository partidoRepository;
    @Autowired
    private TorneoRepository torneoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ArbitroRepository arbitroRepository;

    @Transactional(readOnly = true)
    public List<Partido> getAllPartidos(){
        return partidoRepository.findAll();
    }

    public Partido findPartidoById(Long id){
        return partidoRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Partido no encontrado"));
    }

    public List<Partido> findPartidosByTorneo(Long idTorneo){
        Torneo torneo=torneoRepository.findById(idTorneo)
                .orElseThrow(()->new NotFoundException("Torneo no encontrado"));
        return partidoRepository.findByTorneo(torneo);
    }

    public List<Partido> findPartidosSinJugarPorTorneo(Long idTorneo){
        Torneo torneo=torneoRepository.findById(idTorneo)
                .orElseThrow(()->new NotFoundException("Torneo no encontrado"));
        return partidoRepository.findByTorneoAndJugadoFalse(torneo);
    }

    @Transactional(readOnly = true)
    public List<Partido> findPartidosByEquipo(Long idEquipo) {
        return partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(idEquipo, idEquipo);
    }

    @Transactional(readOnly = true)
    public List<Partido> findPartidosByEquipoAndTorneo(Long idTorneo, Long idEquipo) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        return partidoRepository.findByTorneoAndEquipoLocalIdOrEquipoVisitanteId(torneo, idEquipo, idEquipo);
    }

    @Transactional(readOnly = true)
    public List<Partido> findPartidosSinJugarByEquipoAndTorneo(Long idTorneo, Long idEquipo) {
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        return partidoRepository.findByTorneoAndJugadoFalseAndEquipoLocalIdOrEquipoVisitanteId(torneo, idEquipo, idEquipo);
    }

    @Transactional(readOnly = true)
    public List<Partido> findPartidosPartidosPorArbitro(Long idUsuario){
        Usuario usuario=usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        Arbitro arbitro=arbitroRepository.findByUsuario(usuario)
                .orElseThrow(()-> new NotFoundException("Arbitro no encontrado"));
        return partidoRepository.findByArbitro(arbitro);
    }

    @Transactional(readOnly = true)
    public List<Partido> findPartidosPendientesPorArbitro(Long idUsuario){
        Usuario usuario=usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        Arbitro arbitro=arbitroRepository.findByUsuario(usuario)
                .orElseThrow(()-> new NotFoundException("Arbitro no encontrado"));
        return partidoRepository.findByArbitroAndJugadoFalse(arbitro);
    }

    @Transactional(readOnly = true)
    public List<Partido> findPartidosListosPorArbitros(Long idUsuario){
        Usuario usuario=usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        Arbitro arbitro=arbitroRepository.findByUsuario(usuario)
                .orElseThrow(()-> new NotFoundException("Arbitro no encontrado"));
        return partidoRepository.findByArbitroAndJugadoTrue(arbitro);
    }

    @Transactional(readOnly = true)
    public LocalDate findFechaPartidoMasProximoPorEquipo(Long idEquipo) {
        return partidoRepository.findTopByJugadoFalseAndEquipoLocalIdOrEquipoVisitanteIdOrderByFechaPartidoAsc(idEquipo, idEquipo)
                .map(Partido::getFechaPartido)
                .orElseThrow(() -> new NotFoundException("No hay partidos próximos para este equipo"));
    }

    @Transactional(readOnly = true)
    public LocalDate findFechaPartidoMasProximo() {
        return partidoRepository.findTopByJugadoFalseOrderByFechaPartidoAsc()
                .map(Partido::getFechaPartido)
                .orElseThrow(() -> new NotFoundException("No hay partidos próximos"));
    }

}
