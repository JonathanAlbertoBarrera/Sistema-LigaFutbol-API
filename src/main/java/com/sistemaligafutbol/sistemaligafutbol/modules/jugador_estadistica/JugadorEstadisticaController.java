package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadorestadisticas")
public class JugadorEstadisticaController {

    @Autowired
    private JugadorEstadisticaRepository jugadorEstadisticaRepository;

//    @PostMapping("/registrar/{idPartido}")
//    public ResponseEntity<String> registrarEstadisticas(@PathVariable Long idPartido, @RequestBody List<JugadorEstadisticaDTO> estadisticasDTO) {
//        return ResponseEntity.ok(jugadorEstadisticaService.registrarEstadisticas(idPartido, estadisticasDTO));
//    }

    @GetMapping("/torneo/{idTorneo}")
    public List<ResponseTablaGoleo> obtenerTablaGoleo(@PathVariable Long idTorneo) {
        return jugadorEstadisticaRepository.findByPartido_Torneo_IdOrderByGolesDesc(idTorneo);
    }

//    @PutMapping("/modificar/{idEstadistica}")
//    public ResponseEntity<String> modificarEstadistica(@PathVariable Long idEstadistica, @RequestBody JugadorEstadisticaDTO nuevaEstadisticaDTO) {
//        return ResponseEntity.ok(jugadorEstadisticaService.modificarEstadistica(idEstadistica, nuevaEstadisticaDTO));
//    }
}
