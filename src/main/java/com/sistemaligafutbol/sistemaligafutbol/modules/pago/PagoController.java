package com.sistemaligafutbol.sistemaligafutbol.modules.pago;

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

    @PutMapping("/admin/pagarinscripcion/{idPago}")
    public ResponseEntity<String> completarPagoInscripcion(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.completarPagoInscripcion(idPago));
    }

    @PutMapping("/admin/confirmar/{idPago}")
    public ResponseEntity<String> confirmarPago(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.confirmarPago(idPago));
    }

    @PutMapping("/admin/prorroga/{idPago}")
    public ResponseEntity<String> otorgarProrroga(@PathVariable Long idPago, @RequestParam LocalDate nuevaFechaLimite) {
        return ResponseEntity.ok(pagoService.otorgarProrroga(idPago, nuevaFechaLimite));
    }

    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosPorEquipo(@PathVariable Long idEquipo) {
        return ResponseEntity.ok(pagoService.listarPagosPorEquipo(idEquipo));
    }

    @GetMapping("/equipo/arbitraje/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosArbitrajePorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(pagoService.listarPagosArbitrajePorEquipo(idEquipo));
    }

    @GetMapping("/equipo/cancha/{idEquipo}")
    public ResponseEntity<List<Pago>> listarPagosCachaPorEquipo(@PathVariable Long idEquipo){
        return ResponseEntity.ok(pagoService.listarPagosCachaPorEquipo(idEquipo));
    }
}