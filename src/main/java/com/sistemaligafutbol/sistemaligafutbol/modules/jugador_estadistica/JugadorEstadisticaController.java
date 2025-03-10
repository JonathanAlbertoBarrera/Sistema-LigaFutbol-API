package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadorestadisticas")
public class JugadorEstadisticaController {
    @Autowired
    private JugadorEstadisticaService jugadorEstadisticaService;

    @PostMapping("/registrar/{idPartido}")
    public ResponseEntity<String> registrarEstadisticas(@PathVariable Long idPartido, @RequestBody List<JugadorEstadisticaDTO> estadisticasDTO) {
        return ResponseEntity.ok(jugadorEstadisticaService.registrarEstadisticas(idPartido, estadisticasDTO));
    }

    @PutMapping("/modificar/{idEstadistica}")
    public ResponseEntity<String> modificarEstadistica(@PathVariable Long idEstadistica, @RequestBody JugadorEstadisticaDTO nuevaEstadisticaDTO) {
        return ResponseEntity.ok(jugadorEstadisticaService.modificarEstadistica(idEstadistica, nuevaEstadisticaDTO));
    }
}
