package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/duenos")
public class DuenoController {

    @Autowired
    private DuenoService duenoService;

    @PostMapping
    public Dueno registrarDueno(@RequestBody DuenoRegistroDTO duenoRegistroDTO) {
        return duenoService.registrarDueno(duenoRegistroDTO);
    }
}
