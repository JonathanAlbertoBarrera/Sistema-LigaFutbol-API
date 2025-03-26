package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

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
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @PostMapping
    public ResponseEntity<EquipoResponseDTO> registrarEquipo(@RequestPart("equipo") @Valid EquipoDTO equipoDTO, @RequestPart("imagen") MultipartFile imagen){
        return equipoService.registrarEquipo(equipoDTO,imagen);
    }

    @PostMapping("/movil")
    public ResponseEntity<EquipoResponseDTO> registrarEquipoMovil(@RequestBody @Valid EquipoRegistroRequest request) {
        // Convertir base64 a MultipartFile
        MultipartFile imagenFile = convertBase64ToMultipart(request.getImagen(), "equipo_" + System.currentTimeMillis() + ".jpg");

        EquipoDTO equipoDTO = new EquipoDTO();
        equipoDTO.setNombreEquipo(request.getNombreEquipo());
        equipoDTO.setIdUsuario(request.getIdUsuario());
        equipoDTO.setIdCampo(request.getIdCampo());

        return equipoService.registrarEquipo(equipoDTO, imagenFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizarEquipo(@PathVariable Long id, @RequestPart("equipo") @Valid EquipoDTO equipoDTO, @RequestPart(value = "imagen",required = false) MultipartFile imagen){
        return equipoService.actualizarEquipo(id, equipoDTO, imagen);
    }

    @PutMapping("/movil/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizarEquipoMovil(@PathVariable Long id, @RequestBody @Valid EquipoUpdateRequest request) {

        // Convertir base64 a MultipartFile (si se proporciona imagen)
        MultipartFile imagenFile = request.getImagen() != null ?
                convertBase64ToMultipart(request.getImagen(), "equipo_" + id + ".jpg") :
                null;

        EquipoDTO equipoDTO = new EquipoDTO();
        equipoDTO.setNombreEquipo(request.getNombreEquipo());
        equipoDTO.setIdUsuario(request.getIdUsuario());
        equipoDTO.setIdCampo(request.getIdCampo());

        return equipoService.actualizarEquipo(id, equipoDTO, imagenFile);
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
