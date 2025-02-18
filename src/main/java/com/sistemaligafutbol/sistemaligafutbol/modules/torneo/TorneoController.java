package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @PostMapping
    public ResponseEntity<Torneo> registrarTorneo(@RequestPart("torneo") @Valid TorneoDTO torneoDTO, @RequestPart("imagen") MultipartFile imagen){
        return ResponseEntity.ok(torneoService.registrarTorneo(torneoDTO, imagen));
    }
}
