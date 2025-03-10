package com.sistemaligafutbol.sistemaligafutbol.modules.pago;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
