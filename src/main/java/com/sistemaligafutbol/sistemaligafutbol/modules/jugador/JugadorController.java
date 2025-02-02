package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        List<Jugador> jugadores = jugadorService.obtenerTodosLosJugadores();
        return ResponseEntity.ok(jugadores);
    }

    // OBTENER JUGADOR POR ID CON MENSAJE DE ERROR
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerJugadorPorId(@PathVariable Long id) {
        try {
            Jugador jugador = jugadorService.obtenerJugadorPorId(id);
            return ResponseEntity.ok(jugador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Jugador no encontrado", "id", id));
        }
    }

    // ELIMINAR JUGADOR CON MENSAJE DE ERROR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarJugador(@PathVariable Long id) {
        try {
            jugadorService.eliminarJugador(id);
            return ResponseEntity.ok(Map.of("mensaje", "Jugador eliminado con éxito", "id", id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Jugador no encontrado", "id", id));
        }
    }


    //CREAR UN JUGADOR
    @PostMapping
    public ResponseEntity<Jugador> crearJugador(@RequestPart("jugador") @Valid JugadorDTO jugadorDTO, @RequestPart("imagen") MultipartFile imagen) {
        try {
            Jugador jugadorCreado = jugadorService.crearJugador(jugadorDTO, imagen);
            return ResponseEntity.ok(jugadorCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //MODIFICAR UN JUGADOR
    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizarJugador(@PathVariable Long id, @RequestPart("jugador") @Valid JugadorDTO jugadorDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Jugador jugadorActualizado = jugadorService.actualizarJugador(id, jugadorDTO, imagen);
            return ResponseEntity.ok(jugadorActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
