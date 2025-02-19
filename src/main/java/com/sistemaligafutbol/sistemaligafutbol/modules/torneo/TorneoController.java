package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @PostMapping
    public ResponseEntity<Torneo> registrarTorneo(@RequestPart("torneo") @Valid TorneoDTO torneoDTO, @RequestPart("imagen") MultipartFile imagen){
        return ResponseEntity.ok(torneoService.registrarTorneo(torneoDTO, imagen));
    }

    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodosLosTorneos(){
        return ResponseEntity.ok(torneoService.obtenerTodosLosTorneos());
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
