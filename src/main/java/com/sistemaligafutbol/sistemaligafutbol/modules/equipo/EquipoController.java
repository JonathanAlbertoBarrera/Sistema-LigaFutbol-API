package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @PostMapping
    public ResponseEntity<EquipoResponseDTO> registrarEquipo(@RequestPart("equipo") @Valid EquipoDTO equipoDTO, @RequestPart("imagen") MultipartFile imagen){
        return equipoService.registrarEquipo(equipoDTO,imagen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizarEquipo(@PathVariable Long id, @RequestPart("equipo") @Valid EquipoDTO equipoDTO, @RequestPart(value = "imagen",required = false) MultipartFile imagen){
        return equipoService.actualizarEquipo(id, equipoDTO, imagen);
    }

    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> obtenerTodosLosEquipos(){
        return ResponseEntity.ok(equipoService.obtenerTodosLosEquipos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> obtenerEquipoPorId(@PathVariable Long id){
        return ResponseEntity.ok(equipoService.obtenerEquipoPorId(id));
    }

    @GetMapping("/porDueno/{id}")
    public ResponseEntity<List<EquipoResponseDTO>> obtenerEquiposDelDueno(@PathVariable Long id){
        return ResponseEntity.ok(equipoService.obtenerEquiposPorDueno(id));
    }
}
