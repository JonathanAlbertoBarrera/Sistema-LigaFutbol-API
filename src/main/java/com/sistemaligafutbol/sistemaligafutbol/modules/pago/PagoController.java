package com.sistemaligafutbol.sistemaligafutbol.modules.pago;

import com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos.ConfiguracionPago;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos.ConfiguracionPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ConfiguracionPagoService configuracionPagoService;

    @PutMapping("/admin/confirmar/{idPago}")
    public ResponseEntity<String> confirmarPago(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.pagar(idPago));
    }

    @PutMapping("/admin/precios/{tipoPago}/{nuevoMonto}")
    public ResponseEntity<String> cambiarPrecioTipoPago(@PathVariable String tipoPago, @PathVariable double nuevoMonto){
        return ResponseEntity.ok(configuracionPagoService.actualizarPrecio(tipoPago,nuevoMonto));
    }

    @PutMapping("/admin/prorroga/{idPago}")
    public ResponseEntity<String> otorgarProrroga(@PathVariable Long idPago, @RequestParam LocalDate nuevaFechaLimite) {
        return ResponseEntity.ok(pagoService.otorgarProrroga(idPago, nuevaFechaLimite));
    }

    @GetMapping("/admin/torneo/{idTorneo}")
    public ResponseEntity<List<Pago>> obtenerPagosPorTorneo(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorTorneo(idTorneo));
    }

    @GetMapping("/admin/torneo/pendientes/{idTorneo}")
    public ResponseEntity<List<Pago>> obtenerPagosPendientesPorTorneo(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(pagoService.obtenerPagosPendientesPorTorneo(idTorneo));
    }

    @GetMapping("admin/torneo/confirmados/{idTorneo}")
    public ResponseEntity<List<Pago>> obtenerPagosConfirmadosPorTorneo(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(pagoService.obtenerPagosConfirmadosPorTorneo(idTorneo));
    }

    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosPorEquipo(@PathVariable Long idEquipo) {
        return ResponseEntity.ok(pagoService.listarPagosPorEquipo(idEquipo));
    }

    @GetMapping("/equipo/inscripcion/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosInscripcionPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(pagoService.listarPagosInscripcionPorEquipo(idEquipo));
    }

    @GetMapping("/equipo/arbitraje/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosArbitrajePorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(pagoService.listarPagosArbitrajePorEquipo(idEquipo));
    }

    @GetMapping("/equipo/cancha/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosCachaPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(pagoService.listarPagosCachaPorEquipo(idEquipo));
    }

    // Obtener todos los pagos de un equipo en un torneo específico
    @GetMapping("/equipo/torneo/{idTorneo}/{idEquipo}")
    public ResponseEntity<List<Pago>> obtenerPagosPorEquipoYTorneo(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorEquipoYTorneo(idTorneo, idEquipo));
    }

    // Obtener solo los pagos pendientes de un equipo en un torneo específico
    @GetMapping("/equipo/torneo/pendientes/{idTorneo}/{idEquipo}")
    public ResponseEntity<List<Pago>> obtenerPagosPendientesPorEquipoYTorneo(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
        return ResponseEntity.ok(pagoService.obtenerPagosPendientesPorEquipoYTorneo(idTorneo, idEquipo));
    }

    // Obtener solo los pagos confirmados de un equipo en un torneo específico
    @GetMapping("/equipo/torneo/confirmados/{idTorneo}/{idEquipo}")
    public ResponseEntity<List<Pago>> obtenerPagosConfirmadosPorEquipoYTorneo(@PathVariable Long idTorneo, @PathVariable Long idEquipo) {
        return ResponseEntity.ok(pagoService.obtenerPagosConfirmadosPorEquipoYTorneo(idTorneo, idEquipo));
    }

    @GetMapping("/todos/precios")
    public ResponseEntity<List<ConfiguracionPago>> listarPreciosPagos(){
        return ResponseEntity.ok(configuracionPagoService.findPagos());
    }
}