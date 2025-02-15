package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/duenos")
public class DuenoController {

    @Autowired
    private DuenoService duenoService;

    @PostMapping
    public Dueno registrarDueno(@RequestPart("dueno") @Valid DuenoRegistroDTO duenoRegistroDTO, @RequestPart("imagen") MultipartFile imagen) {
        return duenoService.registrarDueno(duenoRegistroDTO,imagen);
    }
}
