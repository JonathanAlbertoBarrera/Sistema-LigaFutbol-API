package com.sistemaligafutbol.sistemaligafutbol.modules.partido.services;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartidoService {
    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private PartidosLiguillaService partidosLiguillaService; // Para manejar liguilla

    @Transactional
    public String registrarResultado(Long idPartido, PartidoResultadoDTO resultadoDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        if (resultadoDTO.getGolesLocal() < 0 || resultadoDTO.getGolesVisitante() < 0) {
            throw new ValidationException("Los goles no pueden ser negativos.");
        }

        partido.setGolesLocal(resultadoDTO.getGolesLocal());
        partido.setGolesVisitante(resultadoDTO.getGolesVisitante());
        partido.setJugado(true);

        // Guardar descripción si se manda
        if (resultadoDTO.getDescripcionResultado() != null && !resultadoDTO.getDescripcionResultado().isEmpty()) {
            partido.setDescripcionResultado(resultadoDTO.getDescripcionResultado());
        }

        partidoRepository.save(partido);

        // Verificar si el partido pertenece a una liguilla
        if (partido.getTorneo().isEsliguilla()) {
            return registrarResultadoLiguilla(partido.getId(), resultadoDTO);
        }

        return "Resultado registrado correctamente.";
    }

    @Transactional
    public String registrarResultadoLiguilla(Long idPartido, PartidoResultadoDTO resultadoDTO) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        if (resultadoDTO.getGolesLocal() < 0 || resultadoDTO.getGolesVisitante() < 0) {
            throw new ValidationException("Los goles no pueden ser negativos.");
        }

        partido.setGolesLocal(resultadoDTO.getGolesLocal());
        partido.setGolesVisitante(resultadoDTO.getGolesVisitante());
        partido.setJugado(true);

        if (resultadoDTO.getDescripcionResultado() != null && !resultadoDTO.getDescripcionResultado().isEmpty()) {
            partido.setDescripcionResultado(resultadoDTO.getDescripcionResultado());
        }

        partidoRepository.save(partido);

        List<Equipo> equiposFase = partidosLiguillaService.obtenerEquiposFaseActual(partido.getTorneo());
        if (partidosLiguillaService.faseTerminada(partido.getTorneo(), equiposFase)) {
            partidosLiguillaService.generarSiguienteFase(partido.getTorneo());
        }

        return "Resultado registrado correctamente.";
    }

}





