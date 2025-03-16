package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private JugadorReportService jugadorReportService;

    @PostMapping
    public ResponseEntity<Jugador> crearJugador(@RequestPart("jugador") @Valid JugadorDTO jugadorDTO, @RequestPart("imagen") MultipartFile imagen) {
        return ResponseEntity.ok(jugadorService.crearJugador(jugadorDTO, imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizarJugador(@PathVariable Long id, @RequestPart("jugador") @Valid JugadorActualizarDTO jugadorActualizarDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return ResponseEntity.ok(jugadorService.actualizarJugador(id, jugadorActualizarDTO, imagen));
    }

    @PutMapping("/estatus/{id}")
    public ResponseEntity<Jugador> alternarEstatusJugador(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.cambiarEstatusJugador(id));
    }

    @GetMapping
    public ResponseEntity<List<Jugador>> obtenerTodosLosJugadores() {
        return ResponseEntity.ok(jugadorService.obtenerTodosLosJugadores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> obtenerJugadorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.obtenerJugadorPorId(id));
    }

    @GetMapping("/porEquipo/{idEquipo}")
    public ResponseEntity<List<Jugador>> obtenerJugadoresPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(jugadorService.obtenerJugadoresPorEquipo(idEquipo));
    }

    @GetMapping("/credenciales/{idEquipo}/{idTorneo}")
    public ResponseEntity<byte[]> generarCredenciales(@PathVariable Long idEquipo, @PathVariable Long idTorneo) {
        return jugadorReportService.generarCredencialesPDF(idEquipo, idTorneo);
    }

}

