package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @PostMapping
    public ResponseEntity<Torneo> registrarTorneo(@RequestPart("torneo") @Valid TorneoDTO torneoDTO, @RequestPart("imagen") MultipartFile imagen){
        return ResponseEntity.ok(torneoService.registrarTorneo(torneoDTO, imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Torneo> actualizarTorneo(@PathVariable Long id, @RequestPart("torneo") @Valid TorneoDTO torneoDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        return ResponseEntity.ok(torneoService.actualizarTorneo(id, torneoDTO, imagen));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Torneo> cancelarTorneo(@PathVariable Long id, @RequestBody Map<String, String> request) {

        String motivo = request.get("motivoFinalizacion");
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de cancelación es obligatorio.");
        }

        Torneo torneoCancelado = torneoService.cancelarTorneo(id, motivo);
        return ResponseEntity.ok(torneoCancelado);
    }

    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodosLosTorneos(){
        return ResponseEntity.ok(torneoService.obtenerTodosLosTorneos());
    }

    @GetMapping("{id}")
    public ResponseEntity<Torneo> obtenerTorneoporId(@PathVariable Long id){
        return ResponseEntity.ok(torneoService.obtenerTorneo(id));
    }

    @GetMapping("/espera")
    public ResponseEntity<List<Torneo>> obtenerTorneosEnEspera(){
        return ResponseEntity.ok(torneoService.obtenerTorneosEnEspera());
    }

    @GetMapping("/iniciados")
    public ResponseEntity<List<Torneo>> obtenerTorneosIniciados(){
        return ResponseEntity.ok(torneoService.obtenerTorneosIniciados());
    }

    @GetMapping("/finalizados")
    public ResponseEntity<List<Torneo>> obtenerTorneosFinalizados(){
        return ResponseEntity.ok(torneoService.obtenerTorneosFinalizados());
    }
}
