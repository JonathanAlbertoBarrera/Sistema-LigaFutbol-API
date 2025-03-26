package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
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
        return ResponseEntity.ok(arbitroService.obtenerArbitroPorId(id));
    }

    @GetMapping("/poruser/{id}")
    public ResponseEntity<Arbitro> obtenerArbitroPorIdUsuario(@PathVariable Long id){
        return ResponseEntity.ok(arbitroService.obtenerArbiPorIdUsuario(id));
    }

    @PostMapping()
    public ResponseEntity<Arbitro> registrarArbitro(@RequestPart("arbitro") @Valid ArbitroDTO arbitroDTO, @RequestPart("imagen") MultipartFile imagen) {
         return ResponseEntity.ok(arbitroService.registrarArbitro(arbitroDTO,imagen));
    }

    @PostMapping("/movil")
    public ResponseEntity<Arbitro> registrarArbitro(
            @RequestBody @Valid ArbitroRequest request) {

        // 1. Extraer datos
        ArbitroDTO arbitroDTO = new ArbitroDTO();
        arbitroDTO.setEmail(request.getArbitro().getEmail());
        arbitroDTO.setPassword(request.getArbitro().getPassword());
        arbitroDTO.setNombreCompleto(request.getArbitro().getNombreCompleto());

        // 2. Procesar imagen base64
        String base64Image = request.getImagen().replace("data:image/jpeg;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // 3. Convertir a MultipartFile
        MultipartFile imagenFile = new MockMultipartFile(
                "imagen",
                "arbitro.jpg",
                "image/jpeg",
                imageBytes
        );

        return ResponseEntity.ok(arbitroService.registrarArbitro(arbitroDTO, imagenFile));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Arbitro> actualizarArbitro(@PathVariable Long id, @RequestPart("arbitro") @Valid ArbitroActualizarDTO arbitroActualizarDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        return ResponseEntity.ok(arbitroService.actualizarArbitro(id,arbitroActualizarDTO,imagen));
    }

    @PutMapping("/actualizar/movil/{id}")
    public ResponseEntity<Arbitro> actualizarArbitroMovil(@PathVariable Long id, @RequestBody @Valid ArbitroUpdateRequest request) {

        // 1. Crear DTO de actualización
        ArbitroActualizarDTO arbitroActualizarDTO = new ArbitroActualizarDTO();
        arbitroActualizarDTO.setEmail(request.getEmail());
        arbitroActualizarDTO.setNombreCompleto(request.getNombreCompleto());

        // Password solo si se proporciona
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            arbitroActualizarDTO.setPassword(request.getPassword());
        }

        MultipartFile imagenFile = null;

        // 2. Procesar imagen solo si viene en la solicitud
        if (request.getImagen() != null && !request.getImagen().isEmpty()) {
            try {
                // Eliminar el prefijo si existe
                String base64Image = request.getImagen().split(",").length > 1 ?
                        request.getImagen().split(",")[1] :
                        request.getImagen();

                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                // 3. Convertir a MultipartFile
                imagenFile = new MockMultipartFile(
                        "imagen",
                        "arbitro_" + id + ".jpg",
                        "image/jpeg",
                        imageBytes
                );
            } catch (IllegalArgumentException e) {
                throw new ImageValidationException("Formato de imagen base64 no válido");
            }
        }

        // 4. Llamar al servicio
        return ResponseEntity.ok(arbitroService.actualizarArbitro(id, arbitroActualizarDTO, imagenFile));
    }

    @PutMapping("/cambiarEstatus/{idArbitro}")
    public ResponseEntity<String> cambiarEstatusArbitro(@PathVariable Long idArbitro) {
        return ResponseEntity.ok(arbitroService.cambiarEstatusArbitro(idArbitro));
    }

}