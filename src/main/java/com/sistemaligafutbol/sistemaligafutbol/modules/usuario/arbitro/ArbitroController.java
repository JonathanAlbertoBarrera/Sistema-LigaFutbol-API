package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/arbitros")
public class ArbitroController {

    @Autowired
    private ArbitroService arbitroService;

    @GetMapping
    public ResponseEntity<List<Arbitro>> obtenerTodosLosJugadores(){
        return ResponseEntity.ok(arbitroService.obtenerTodosLosArbitros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arbitro> obtenerArbitroPorId(@PathVariable Long id){
        return ResponseEntity.ok(arbitroService.obtenerJugadorPorId(id));
    }

    @PostMapping
    public ResponseEntity<Arbitro> registrarArbitro(@RequestPart("arbitro") @Valid ArbitroDTO arbitroDTO, @RequestPart("imagen") MultipartFile imagen) {
         return ResponseEntity.ok(arbitroService.registrarArbitro(arbitroDTO,imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Arbitro> actualizarArbitro(@PathVariable Long id, @RequestPart("arbitro") @Valid ArbitroDTO arbitroDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        return ResponseEntity.ok(arbitroService.actualizarArbitro(id,arbitroDTO,imagen));
    }

}