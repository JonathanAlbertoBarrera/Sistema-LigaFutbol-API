package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/arbitros")
public class ArbitroController {

    @Autowired
    private ArbitroService arbitroService;

    @PostMapping
    public ResponseEntity<Arbitro> registrarArbitro(@RequestPart("arbitro") @Valid ArbitroDTO arbitroDTO, @RequestPart("imagen") MultipartFile imagen) {
         return ResponseEntity.ok(arbitroService.registrarArbitro(arbitroDTO,imagen));
    }
}