package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

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

    @PostMapping("/movil")
    public Dueno registrarDuenoMovil(@RequestBody @Valid DuenoRegistroRequest request) {
        // Convertir base64 a MultipartFile
        MultipartFile imagenFile = convertBase64ToMultipart(request.getImagen(), "dueno_registro.jpg");
        return duenoService.registrarDueno(
                new DuenoRegistroDTO(
                        request.getEmail(),
                        request.getPassword(),
                        request.getNombreCompleto()
                ),
                imagenFile
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueno> actualizarArbitro(@PathVariable Long id, @RequestPart("dueno") @Valid DuenoActualizarDTO duenoActualizarDTO, @RequestPart(value = "imagen",required = false) MultipartFile imagen){
        return ResponseEntity.ok(duenoService.actualizarDueno(id,duenoActualizarDTO,imagen));
    }

    @PutMapping("/movil/{id}")
    public ResponseEntity<Dueno> actualizarDuenoMovil(@PathVariable Long id, @RequestBody @Valid DuenoUpdateRequest request) {

        // Convertir base64 a MultipartFile (si se proporciona imagen)
        MultipartFile imagenFile = request.getImagen() != null ?
                convertBase64ToMultipart(request.getImagen(), "dueno_" + id + ".jpg") :
                null;

        return ResponseEntity.ok(duenoService.actualizarDueno(id, new DuenoActualizarDTO(request.getEmail(), request.getNombreCompleto(), request.getPassword()), imagenFile));
    }

    @PutMapping("/estatus/{idDueno}")
    public ResponseEntity<String> cambiarEstatus(@PathVariable Long idDueno){
        return ResponseEntity.ok(duenoService.cambiarEstatusDueno(idDueno));
    }

    private MultipartFile convertBase64ToMultipart(String base64Image, String filename) {
        try {
            // Eliminar el prefijo si existe
            String base64Data = base64Image.contains(",") ?
                    base64Image.split(",")[1] :
                    base64Image;

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            return new MockMultipartFile(
                    "imagen",
                    filename,
                    "image/jpeg",
                    imageBytes
            );
        } catch (Exception e) {
            throw new ImageValidationException("Error al procesar la imagen: " + e.getMessage());
        }
    }
}
