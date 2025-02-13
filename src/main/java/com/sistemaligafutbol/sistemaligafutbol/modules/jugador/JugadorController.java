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

    @GetMapping
    public ResponseEntity<List<Jugador>> obtenerTodosLosJugadores() {
        return ResponseEntity.ok(jugadorService.obtenerTodosLosJugadores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> obtenerJugadorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.obtenerJugadorPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarJugador(@PathVariable Long id) {
        jugadorService.eliminarJugador(id);
        return ResponseEntity.ok(Map.of("mensaje", "Jugador eliminado con éxito", "id", id.toString()));
    }

    @PostMapping
    public ResponseEntity<Jugador> crearJugador(@RequestPart("jugador") @Valid JugadorDTO jugadorDTO, @RequestPart("imagen") MultipartFile imagen) {
        return ResponseEntity.ok(jugadorService.crearJugador(jugadorDTO, imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizarJugador(@PathVariable Long id, @RequestPart("jugador") @Valid JugadorActualizarDTO jugadorActualizarDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return ResponseEntity.ok(jugadorService.actualizarJugador(id, jugadorActualizarDTO, imagen));
    }
}

