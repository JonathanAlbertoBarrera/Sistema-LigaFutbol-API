package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/arbitros")
public class ArbitroController {

    @Autowired
    private ArbitroService arbitroService;

    @PostMapping
    public Arbitro registrarArbitro(@RequestBody ArbitroRegistroDTO arbitroRegistroDTO) {
        return arbitroService.registrarArbitro(arbitroRegistroDTO);
    }
}