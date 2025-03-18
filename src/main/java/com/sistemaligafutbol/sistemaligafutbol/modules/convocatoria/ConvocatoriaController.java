package com.sistemaligafutbol.sistemaligafutbol.modules.convocatoria;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/convocatorias")
public class ConvocatoriaController {

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @PostMapping("/publicar/{torneoId}")
    public ResponseEntity<String> publicarConvocatoria(@PathVariable Long torneoId) {
        return ResponseEntity.ok(convocatoriaService.publicarConvocatoria(torneoId));
    }

    @GetMapping("/activa")
    public ResponseEntity<String> obtenerConvocatoriaActiva() {
        return ResponseEntity.ok(convocatoriaService.obtenerConvocatoriaActiva());
    }
}
