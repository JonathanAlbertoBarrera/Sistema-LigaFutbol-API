package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidoService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidosLiguillaService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidosTorneoRegularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {
    @Autowired
    private PartidoService partidoService;

    @Autowired
    private PartidosLiguillaService partidosLiguillaService;

    @Autowired
    private PartidosTorneoRegularService partidosTorneoRegularService;

    @PostMapping("/iniciartorneo/{idTorneo}")
    public ResponseEntity<String> iniciarTorneo(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(partidosTorneoRegularService.iniciarTorneo(idTorneo));
    }

    @PostMapping("/iniciarliguilla/{idTorneo}")
    public ResponseEntity<String> iniciarLiguilla(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(partidosLiguillaService.iniciarLiguilla(idTorneo));
    }

//    @PostMapping("/registraresultado/{idPartido}")
//    public ResponseEntity<String> registrarResultado(@PathVariable Long idPartido, @RequestBody PartidoResultadoDTO resultadoDTO) {
//        return ResponseEntity.ok(partidoService.registrarResultado(idPartido, resultadoDTO));
//    }

//    @PutMapping("/modificar/{idPartido}")
//    public ResponseEntity<String> modificarPartido(@PathVariable Long idPartido, @RequestBody PartidoModificarDTO partidoDTO) {
//        return ResponseEntity.ok(partidoService.modificarPartido(idPartido, partidoDTO));
//    }

}
