package com.sistemaligafutbol.sistemaligafutbol.modules.pago;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.Solicitud;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    //PARA GENERAR PAGOS A LOS PARTIDOS
    public void generarPagoPorPartido(Partido partido) {
        Date fechaLimite = java.sql.Date.valueOf(partido.getFechaPartido().minusDays(4));

        if (!pagoRepository.existsByEquipoAndTipoPagoAndFechaLimitePago(partido.getEquipoLocal(), "Cancha", fechaLimite)) {
            generarPago(partido.getEquipoLocal(), "Cancha", 200, partido);
        }

        if (!pagoRepository.existsByEquipoAndTipoPagoAndFechaLimitePago(partido.getEquipoLocal(), "Arbitraje", fechaLimite)) {
            generarPago(partido.getEquipoLocal(), "Arbitraje", 100, partido);
        }

        if (!pagoRepository.existsByEquipoAndTipoPagoAndFechaLimitePago(partido.getEquipoVisitante(), "Arbitraje", fechaLimite)) {
            generarPago(partido.getEquipoVisitante(), "Arbitraje", 100, partido);
        }
    }

    public void generarPago(Equipo equipo, String tipoPago, int monto, Partido partido) {
        Pago pago = new Pago();
        pago.setEquipo(equipo);
        pago.setTipoPago(tipoPago);
        pago.setMonto(monto);
        pago.setDescripcion(tipoPago + " - " + partido.getEquipoLocal().getNombreEquipo() + " vs " +
                partido.getEquipoVisitante().getNombreEquipo() + " - " + partido.getFechaPartido());
        pago.setFechaLimitePago(java.sql.Date.valueOf(partido.getFechaPartido().minusDays(4))); // Miércoles antes del partido
        pagoRepository.save(pago);
    }

    @Transactional
    public String completarPagoInscripcion(Long idPago) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        if (pago.isEstatusPago()) {
            throw new ValidationException("El pago ya ha sido completado.");
        }

        // Obtener la solicitud asociada al equipo
        Solicitud solicitud = solicitudRepository.findByEquipoAndTorneo(pago.getEquipo(),
                        solicitudRepository.findByEquipo(pago.getEquipo())
                                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"))
                                .getTorneo())
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        Torneo torneo = solicitud.getTorneo();

        // Verificar si el torneo ya está lleno
        long equiposAceptados = solicitudRepository.countByTorneoAndResolucionTrueAndInscripcionEstatusTrue(torneo);
        if (equiposAceptados >= torneo.getMaxEquipos()) {
            solicitudRepository.deleteByEquipoAndTorneo(pago.getEquipo(), torneo);
            pagoRepository.delete(pago);
            pagoRepository.flush();
            throw new ValidationException("El torneo ya ha alcanzado el número máximo de equipos permitidos. La solicitud y el pago han sido eliminados.");
        }

        // Completar el pago
        pago.setEstatusPago(true);
        pago.setFechaPago(new Date());
        pagoRepository.save(pago);

        // Cambiar inscripcionEstatus a true
        solicitud.setInscripcionEstatus(true);
        solicitudRepository.save(solicitud);

        // Verificar si se alcanzó el máximo de equipos
        equiposAceptados = solicitudRepository.countByTorneoAndResolucionTrueAndInscripcionEstatusTrue(torneo);
        if (equiposAceptados >= torneo.getMaxEquipos()) {
            torneo.setEstatusLlenado(true);
            torneoRepository.save(torneo);
        }

        return "Pago de inscripción completado correctamente.";
    }

    //ESTO SERIA PARA PAGOS DE TIPO ARBITRAJE Y CANCHA
    @Transactional
    public String confirmarPago(Long idPago) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        pago.setEstatusPago(true);
        pagoRepository.save(pago);
        return "Pago confirmado correctamente.";
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPagosPorEquipo(Long idEquipo){
        Equipo equipo=equipoRepository.findById(idEquipo)
                .orElseThrow(()-> new NotFoundException("Equipo no encontrado"));

        return pagoRepository.findByEquipo(equipo);
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPagosArbitrajePorEquipo(Long idEquipo){
        Equipo equipo=equipoRepository.findById(idEquipo)
                .orElseThrow(()-> new NotFoundException("Equipo no encontrado"));

        return pagoRepository.findAllByEquipoAndTipoPago(equipo,"Arbitraje");
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPagosCachaPorEquipo(Long idEquipo){
        Equipo equipo=equipoRepository.findById(idEquipo)
                .orElseThrow(()-> new NotFoundException("Equipo no encontrado"));

        return pagoRepository.findAllByEquipoAndTipoPago(equipo,"Cancha");
    }

    @Transactional
    public String otorgarProrroga(Long idPago, LocalDate nuevaFechaLimite) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        if (nuevaFechaLimite.isBefore(LocalDate.now())) {
            throw new ValidationException("La nueva fecha límite no puede ser anterior a la fecha actual.");
        }

        pago.setFechaLimitePago(Date.from(nuevaFechaLimite.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        pagoRepository.save(pago);

        return "Prórroga otorgada correctamente. Nueva fecha límite: " + nuevaFechaLimite;
    }
}
