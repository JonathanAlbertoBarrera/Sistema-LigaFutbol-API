package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canchas")
public class CanchaController {
    @Autowired
    private CanchaService canchaService;

    @PostMapping
    public ResponseEntity<Cancha> crearCancha(@RequestBody @Valid CanchaDTO canchaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(canchaService.crearCancha(canchaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cancha> actualizarCancha(@PathVariable Long id, @RequestBody @Valid CanchaDTO canchaDTO) {
        return ResponseEntity.ok(canchaService.actualizarCancha(id, canchaDTO));
    }

    @GetMapping
    public ResponseEntity<List<Cancha>> obtenerCanchas() {
        return ResponseEntity.ok(canchaService.obtenerCanchas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cancha> obtenerCanchaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(canchaService.obtenerCanchaPorId(id));
    }
}

