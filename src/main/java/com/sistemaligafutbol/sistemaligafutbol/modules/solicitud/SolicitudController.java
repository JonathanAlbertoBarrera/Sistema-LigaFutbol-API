package com.sistemaligafutbol.sistemaligafutbol.modules.solicitud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/admin")
    public ResponseEntity<List<SolicitudDTO>> listarTodasSolicitudes() {
        return ResponseEntity.ok(solicitudService.listarTodasSolicitudes());
    }

    @GetMapping("admin/pendientes")
    public ResponseEntity<List<SolicitudDTO>> listarTodasSolicitudesPendientes(){
        return ResponseEntity.ok(solicitudService.listarTodasSolicitudesPendientes());
    }

    @GetMapping("admin/todasPorTorneo/{torneoId}")
    public ResponseEntity<List<SolicitudDTO>> listarTodasSolicitudesPorTorneo(@PathVariable Long torneoId){
        return ResponseEntity.ok(solicitudService.listarTodasSolicitudesPorTorneo(torneoId));
    }

    @GetMapping("admin/pendientesPorTorneo/{torneoId}")
    public ResponseEntity<List<SolicitudDTO>> listarSolicitudesPendientesPorTorneo(@PathVariable Long torneoId){
        return ResponseEntity.ok(solicitudService.listarSolicitudesPendientesPorTorneo(torneoId));
    }

    @GetMapping("admin/aceptadasPorTorneo/{torneoId}")
    public ResponseEntity<List<SolicitudDTO>> listarSolicitudesAceptadasPorTorneo(@PathVariable Long torneoId){
        return ResponseEntity.ok(solicitudService.listarSolicitudesAceptadasPorTorneo(torneoId));
    }

    @GetMapping("/dueno/{idUsuario}")
    public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorDueno(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorDueno(idUsuario));
    }

    @PostMapping("/{idEquipo}/{idTorneo}")
    public ResponseEntity<SolicitudDTO> crearSolicitud(@PathVariable Long idEquipo, @PathVariable Long idTorneo) {
        return ResponseEntity.ok(solicitudService.crearSolicitud(idEquipo, idTorneo));
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<String> aceptarSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.aceptarSolicitud(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<String> rechazarSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.rechazarSolicitud(id));
    }

}
