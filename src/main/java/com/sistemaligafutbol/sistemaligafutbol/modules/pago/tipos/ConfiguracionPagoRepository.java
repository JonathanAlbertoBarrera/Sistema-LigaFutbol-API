package com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionPagoRepository extends JpaRepository<ConfiguracionPago, Long> {
    Optional<ConfiguracionPago> findByTipoPago(String tipoPago);
}

