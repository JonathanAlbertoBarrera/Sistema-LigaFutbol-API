package com.sistemaligafutbol.sistemaligafutbol.modules.campo;

import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/campos")
public class CampoController {
    @Autowired
    private CampoService campoService;

    @PostMapping
    public ResponseEntity<Campo> crearCampo(@RequestBody @Valid CampoDTO campoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campoService.crearCampo(campoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campo> actualizarCampo(@PathVariable Long id, @RequestBody @Valid CampoDTO campoDTO) {
        return ResponseEntity.ok(campoService.actualizarCampo(id, campoDTO));
    }

    @GetMapping
    public ResponseEntity<List<Campo>> obtenerCampos() {
        return ResponseEntity.ok(campoService.obtenerCampos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Campo>> obtenerCamposActivos(){
        return ResponseEntity.ok(campoService.obtenerCamposActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campo> obtenerCampoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(campoService.obtenerCampoPorId(id));
    }

    @PutMapping("/estatus/{idCampo}")
    public ResponseEntity<String> cambiarEstatus(@PathVariable Long idCampo){
        return ResponseEntity.ok(campoService.cambiarEstatusCampo(idCampo));
    }
}

