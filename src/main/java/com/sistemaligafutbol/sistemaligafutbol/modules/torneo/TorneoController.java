package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @PostMapping
    public ResponseEntity<Torneo> registrarTorneo(@RequestPart("torneo") @Valid TorneoDTO torneoDTO, @RequestPart("imagen") MultipartFile imagen){
        return ResponseEntity.ok(torneoService.registrarTorneo(torneoDTO, imagen));
    }

    @PostMapping("/movil")
    public ResponseEntity<Torneo> registrarTorneoMovil(@RequestBody @Valid TorneoRegistroRequest request) {
        // Convertir base64 a MultipartFile
        MultipartFile imagenFile = convertBase64ToMultipart(request.getImagen(), "torneo_" + System.currentTimeMillis() + ".jpg");

        TorneoDTO torneoDTO = new TorneoDTO();
        torneoDTO.setNombreTorneo(request.getNombreTorneo());
        torneoDTO.setDescripcion(request.getDescripcion());
        torneoDTO.setFechaInicio(request.getFechaInicio());
        torneoDTO.setMaxEquipos(request.getMaxEquipos());
        torneoDTO.setMinEquipos(request.getMinEquipos());
        torneoDTO.setEquiposLiguilla(request.getEquiposLiguilla());
        torneoDTO.setVueltas(request.getVueltas());
        torneoDTO.setPremio(request.getPremio());

        return ResponseEntity.ok(torneoService.registrarTorneo(torneoDTO, imagenFile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Torneo> actualizarTorneo(@PathVariable Long id, @RequestPart("torneo") @Valid TorneoDTO torneoDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        return ResponseEntity.ok(torneoService.actualizarTorneo(id, torneoDTO, imagen));
    }

    @PutMapping("/movil/{id}")
    public ResponseEntity<Torneo> actualizarTorneoMovil(@PathVariable Long id, @RequestBody @Valid TorneoUpdateRequest request) {

        // Convertir base64 a MultipartFile (si se proporciona imagen)
        MultipartFile imagenFile = request.getImagen() != null ?
                convertBase64ToMultipart(request.getImagen(), "torneo_" + id + ".jpg") :
                null;

        TorneoDTO torneoDTO = new TorneoDTO();
        torneoDTO.setNombreTorneo(request.getNombreTorneo());
        torneoDTO.setDescripcion(request.getDescripcion());
        torneoDTO.setFechaInicio(request.getFechaInicio());
        torneoDTO.setMaxEquipos(request.getMaxEquipos());
        torneoDTO.setMinEquipos(request.getMinEquipos());
        torneoDTO.setEquiposLiguilla(request.getEquiposLiguilla());
        torneoDTO.setVueltas(request.getVueltas());
        torneoDTO.setPremio(request.getPremio());

        return ResponseEntity.ok(torneoService.actualizarTorneo(id, torneoDTO, imagenFile));
    }


    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Torneo> cancelarTorneo(@PathVariable Long id, @RequestBody Map<String, String> request) {

        String motivo = request.get("motivoFinalizacion");
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de cancelación es obligatorio.");
        }

        Torneo torneoCancelado = torneoService.cancelarTorneo(id, motivo);
        return ResponseEntity.ok(torneoCancelado);
    }

    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodosLosTorneos(){
        return ResponseEntity.ok(torneoService.obtenerTodosLosTorneos());
    }

    @GetMapping("{id}")
    public ResponseEntity<Torneo> obtenerTorneoporId(@PathVariable Long id){
        return ResponseEntity.ok(torneoService.obtenerTorneo(id));
    }

    @GetMapping("/espera")
    public ResponseEntity<List<Torneo>> obtenerTorneosEnEspera(){
        return ResponseEntity.ok(torneoService.obtenerTorneosEnEspera());
    }

    @GetMapping("/iniciados")
    public ResponseEntity<List<Torneo>> obtenerTorneosIniciados(){
        return ResponseEntity.ok(torneoService.obtenerTorneosIniciados());
    }

    @GetMapping("/finalizados")
    public ResponseEntity<List<Torneo>> obtenerTorneosFinalizados(){
        return ResponseEntity.ok(torneoService.obtenerTorneosFinalizados());
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
