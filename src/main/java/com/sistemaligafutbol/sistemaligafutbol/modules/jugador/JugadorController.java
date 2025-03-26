package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;


import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private JugadorReportService jugadorReportService;

    @PostMapping
    public ResponseEntity<Jugador> crearJugador(@RequestPart("jugador") @Valid JugadorDTO jugadorDTO, @RequestPart("imagen") MultipartFile imagen) {
        return ResponseEntity.ok(jugadorService.crearJugador(jugadorDTO, imagen));
    }

    @PostMapping("/movil")
    public ResponseEntity<Jugador> crearJugadorMovil(@RequestBody @Valid JugadorRegistroRequest request) {
        // Convertir base64 a MultipartFile
        MultipartFile imagenFile = convertBase64ToMultipart(request.getImagen(), "jugador_" + System.currentTimeMillis() + ".jpg");

        JugadorDTO jugadorDTO = new JugadorDTO();
        jugadorDTO.setNombreCompleto(request.getNombreCompleto());
        jugadorDTO.setFechaNacimiento(request.getFechaNacimiento());
        jugadorDTO.setNumero_camiseta(request.getNumero_camiseta());
        jugadorDTO.setIdEquipo(request.getIdEquipo());

        return ResponseEntity.ok(jugadorService.crearJugador(jugadorDTO, imagenFile));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizarJugador(@PathVariable Long id, @RequestPart("jugador") @Valid JugadorActualizarDTO jugadorActualizarDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return ResponseEntity.ok(jugadorService.actualizarJugador(id, jugadorActualizarDTO, imagen));
    }

    @PutMapping("/movil/{id}")
    public ResponseEntity<Jugador> actualizarJugadorMovil(@PathVariable Long id, @RequestBody @Valid JugadorUpdateRequest request) {

        // Convertir base64 a MultipartFile (si se proporciona imagen)
        MultipartFile imagenFile = request.getImagen() != null ?
                convertBase64ToMultipart(request.getImagen(), "jugador_" + id + ".jpg") :
                null;

        JugadorActualizarDTO jugadorDTO = new JugadorActualizarDTO();
        jugadorDTO.setNombreCompleto(request.getNombreCompleto());
        jugadorDTO.setFechaNacimiento(request.getFechaNacimiento());
        jugadorDTO.setNumero_camiseta(request.getNumero_camiseta());

        return ResponseEntity.ok(jugadorService.actualizarJugador(id, jugadorDTO, imagenFile));
    }

    @PutMapping("/estatus/{id}")
    public ResponseEntity<Jugador> alternarEstatusJugador(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.cambiarEstatusJugador(id));
    }

    @GetMapping
    public ResponseEntity<List<Jugador>> obtenerTodosLosJugadores() {
        return ResponseEntity.ok(jugadorService.obtenerTodosLosJugadores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> obtenerJugadorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.obtenerJugadorPorId(id));
    }

    @GetMapping("/porEquipo/{idEquipo}")
    public ResponseEntity<List<Jugador>> obtenerJugadoresPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(jugadorService.obtenerJugadoresPorEquipo(idEquipo));
    }

    @GetMapping("/porEquipo/filtrados/{idEquipo}")
    public ResponseEntity<List<Jugador>> jugadoresActivosyNoExpulsadosPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(jugadorService.jugadoresActivosyNoExpulsadosPorEquipo(idEquipo));
    }

    @GetMapping("/credenciales/{idEquipo}/{idTorneo}")
    public ResponseEntity<byte[]> generarCredenciales(@PathVariable Long idEquipo, @PathVariable Long idTorneo) {
        return jugadorReportService.generarCredencialesPDF(idEquipo, idTorneo);
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

