package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/duenos")
public class DuenoController {

    @Autowired
    private DuenoService duenoService;

    @GetMapping
    public ResponseEntity<List<Dueno>> obtenerTodosLosDuenos(){
        return ResponseEntity.ok(duenoService.obtenerTodosLosDuenos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueno> obtenerDuenoPorId(@PathVariable Long id){
        return ResponseEntity.ok(duenoService.obtenerArbitroPorId(id));
    }

    @GetMapping("/porusuario/{idUsuario}")
    public ResponseEntity<Dueno> obtenerDuenoPorIdUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(duenoService.obtenerDuenoPorIdUsuario(idUsuario));
    }

    @PostMapping
    public Dueno registrarDueno(@RequestPart("dueno") @Valid DuenoRegistroDTO duenoRegistroDTO, @RequestPart("imagen") MultipartFile imagen) {
        return duenoService.registrarDueno(duenoRegistroDTO,imagen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueno> actualizarArbitro(@PathVariable Long id, @RequestPart("dueno") @Valid DuenoActualizarDTO duenoActualizarDTO, @RequestPart(value = "imagen",required = false) MultipartFile imagen){
        return ResponseEntity.ok(duenoService.actualizarDueno(id,duenoActualizarDTO,imagen));
    }

    @PutMapping("/estatus/{idDueno}")
    public ResponseEntity<String> cambiarEstatus(@PathVariable Long idDueno){
        return ResponseEntity.ok(duenoService.cambiarEstatusDueno(idDueno));
    }
}
