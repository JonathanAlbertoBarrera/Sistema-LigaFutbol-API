package com.sistemaligafutbol.sistemaligafutbol.modules.pago;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago,Long> {
    // Buscar pago por equipo y tipo de pago
    Optional<Pago> findByEquipoAndTipoPago(Equipo equipo, String tipoPago);

    List<Pago> findAllByEquipoAndTipoPago(Equipo equipo, String tipoPago);

    // Buscar pagos pendientes por equipo
    List<Pago> findByEquipoAndEstatusPagoFalse(Equipo equipo);

    // Buscar pagos por equipo
    List<Pago> findByEquipo(Equipo equipo);

    boolean existsByEquipoAndTipoPagoAndFechaLimitePago(Equipo equipo, String tipoPago, Date fechaLimitePago);

    // Obtener pagos de todos los equipos que están en un torneo
    @Query("SELECT p FROM Pago p WHERE p.equipo IN (SELECT s.equipo FROM Solicitud s WHERE s.torneo.id = :idTorneo AND s.resolucion = true AND s.inscripcionEstatus = true)")
    List<Pago> findPagosPorTorneo(@Param("idTorneo") Long idTorneo);

    // Obtener pagos pendientes por torneo
    @Query("SELECT p FROM Pago p WHERE p.estatusPago = false AND p.equipo IN (SELECT s.equipo FROM Solicitud s WHERE s.torneo.id = :idTorneo AND s.resolucion = true AND s.inscripcionEstatus = true)")
    List<Pago> findPagosPendientesPorTorneo(@Param("idTorneo") Long idTorneo);

    // Obtener pagos confirmados por torneo
    @Query("SELECT p FROM Pago p WHERE p.estatusPago = true AND p.equipo IN (SELECT s.equipo FROM Solicitud s WHERE s.torneo.id = :idTorneo AND s.resolucion = true AND s.inscripcionEstatus = true)")
    List<Pago> findPagosConfirmadosPorTorneo(@Param("idTorneo") Long idTorneo);

    @Query("SELECT p FROM Pago p WHERE p.equipo IN (SELECT s.equipo FROM Solicitud s WHERE s.torneo = :torneo) AND p.equipo = :equipo")
    List<Pago> findByTorneoAndEquipo(@Param("torneo") Torneo torneo, @Param("equipo") Equipo equipo);

    @Query("SELECT p FROM Pago p WHERE p.equipo IN (SELECT s.equipo FROM Solicitud s WHERE s.torneo = :torneo) AND p.equipo = :equipo AND p.estatusPago = false")
    List<Pago> findByTorneoAndEquipoAndEstatusPagoFalse(@Param("torneo") Torneo torneo, @Param("equipo") Equipo equipo);

    @Query("SELECT p FROM Pago p WHERE p.equipo IN (SELECT s.equipo FROM Solicitud s WHERE s.torneo = :torneo) AND p.equipo = :equipo AND p.estatusPago = true")
    List<Pago> findByTorneoAndEquipoAndEstatusPagoTrue(@Param("torneo") Torneo torneo, @Param("equipo") Equipo equipo);

}
