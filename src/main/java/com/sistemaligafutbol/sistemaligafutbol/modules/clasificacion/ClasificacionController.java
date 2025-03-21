package com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tabla-clasificacion")
public class ClasificacionController {
    @Autowired
    private ClasificacionRepository clasificacionRepository;
    @Autowired
    private TorneoRepository torneoRepository;

    @GetMapping("/{idTorneo}")
    public List<ClasificacionDTO> obtenerClasificacion(@PathVariable Long idTorneo) {
        // Obtener la clasificación ordenada por puntos y demás criterios
        Torneo torneo=torneoRepository.findById(idTorneo)
                .orElseThrow(()->new NotFoundException("Torneo no encontrado"));
        List<Clasificacion> clasificaciones = clasificacionRepository.findClasificacionesByTorneo(torneo);

        // Convertir la lista de Clasificacion a ClasificacionDTO
        List<ClasificacionDTO> clasificacionDTOs = new ArrayList<>();
        for (Clasificacion clasificacion : clasificaciones) {
            ClasificacionDTO dto = new ClasificacionDTO(
                    clasificacion.getId(),
                    clasificacion.getPuntos(),
                    clasificacion.getPartidosGanados(),
                    clasificacion.getPartidosPerdidos(),
                    clasificacion.getPartidosEmpatados(),
                    clasificacion.getGolesAFavor(),
                    clasificacion.getGolesEnContra(),
                    clasificacion.getEquipo().getId(),
                    clasificacion.getEquipo().getNombreEquipo(),  // Solo el nombre del equipo
                    clasificacion.getEquipo().getLogo()           // Solo el logo del equipo
            );
            clasificacionDTOs.add(dto);
        }
        return clasificacionDTOs;
    }


}
