package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidoGetServices;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidoService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidosLiguillaService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.services.PartidosTorneoRegularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {
    @Autowired
    private PartidoService partidoService;

    @Autowired
    private PartidosTorneoRegularService partidosTorneoRegularService;

    @Autowired
    private PartidoGetServices partidoGetServices;

    @PostMapping("/admin/iniciartorneo/{idTorneo}")
    public ResponseEntity<String> iniciarTorneo(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(partidosTorneoRegularService.iniciarTorneo(idTorneo));
    }

    @PostMapping("/arbitro/registraresultado/{idPartido}")
    public ResponseEntity<String> registrarResultado(@PathVariable Long idPartido, @RequestBody PartidoResultadoDTO resultadoDTO) {
        return ResponseEntity.ok(partidoService.registrarResultado(idPartido, resultadoDTO));
    }

//    @PutMapping("/modificar/{idPartido}")
//    public ResponseEntity<String> modificarPartido(@PathVariable Long idPartido, @RequestBody PartidoModificarDTO partidoDTO) {
//        return ResponseEntity.ok(partidoService.modificarPartido(idPartido, partidoDTO));
//    }

    //---  GETS
    @GetMapping("/todos")
    public ResponseEntity<List<Partido>> todosLosPartidos(){
        return ResponseEntity.ok(partidoGetServices.getAllPartidos());
    }

    @GetMapping("/todos/{idPartido}")
    public ResponseEntity<Partido> verUnPartido(@PathVariable Long idPartido){
        return ResponseEntity.ok(partidoGetServices.findPartidoById(idPartido));
    }

    @GetMapping("/todos/portorneo/{idTorneo}")
    public ResponseEntity<List<Partido>> findPartidosPorTorneo(@PathVariable Long idTorneo){
        return ResponseEntity.ok(partidoGetServices.findPartidosByTorneo(idTorneo));
    }

    @GetMapping("/todos/portorneo/nojugados/{idTorneo}")
    public ResponseEntity<List<Partido>> findPartidosSinJugarPorTorneo(@PathVariable Long idTorneo){
        return ResponseEntity.ok(partidoGetServices.findPartidosSinJugarPorTorneo(idTorneo));
    }

    @GetMapping("/todos/porequipo/{idEquipo}")
    public ResponseEntity<List<Partido>> findPartidosByEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(partidoGetServices.findPartidosByEquipo(idEquipo));
    }

    @GetMapping("/todos/porequipo/{idTorneo}/{idEquipo}")
    public ResponseEntity<List<Partido>> findPartidosByEquipoAndTorneo(@PathVariable Long idTorneo,@PathVariable Long idEquipo){
        return ResponseEntity.ok(partidoGetServices.findPartidosByEquipoAndTorneo(idTorneo,idEquipo));
    }

    @GetMapping("/todos/porequipo/pendientes/{idTorneo}/{idEquipo}")
    public ResponseEntity<List<Partido>> findPartidosSinJugarByEquipoAndTorneo(@PathVariable Long idTorneo,@PathVariable Long idEquipo){
        return ResponseEntity.ok(partidoGetServices.findPartidosSinJugarByEquipoAndTorneo(idTorneo,idEquipo));
    }

    @GetMapping("/todos/masproximo")
    public ResponseEntity<LocalDate> buscarFechaPartidoSinJugarMasProximo(){
        return ResponseEntity.ok(partidoGetServices.findFechaPartidoMasProximo());
    }

    @GetMapping("/todos/masproximo/{idEquipo}")
    public ResponseEntity<LocalDate> buscarfechaPartidoSinJugarDeUnEquipoMasproximo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(partidoGetServices.findFechaPartidoMasProximoPorEquipo(idEquipo));
    }

}
