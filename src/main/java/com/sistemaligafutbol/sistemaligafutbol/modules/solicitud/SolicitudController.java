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

    @GetMapping("admin/confirmados/{torneoId}")
    public ResponseEntity<List<SolicitudDTO>> listarEquiposConfirmadosPorTorneo(@PathVariable Long torneoId){
        return ResponseEntity.ok(solicitudService.listarEquiposConfirmadosPorTorneo(torneoId));
    }

    @GetMapping("/dueno/pordueno/{idUsuario}")
    public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorDueno(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorDueno(idUsuario));
    }

    @GetMapping("/dueno/byid/{idSolicitud}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitudById(@PathVariable Long idSolicitud){
        return ResponseEntity.ok(solicitudService.obtenerSolicitudPorId(idSolicitud));
    }

    @GetMapping("/dueno/porequipo/{idEquipo}")
    public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorEquipo(idEquipo));
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
