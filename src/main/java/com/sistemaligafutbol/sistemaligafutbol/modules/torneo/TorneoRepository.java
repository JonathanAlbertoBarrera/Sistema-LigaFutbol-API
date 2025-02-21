package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo,Long> {
    List<Torneo> findByEstatusTorneoTrueAndIniciadoFalse();
    List<Torneo> findByEstatusTorneoTrueAndIniciadoTrue();
    List<Torneo> findByEstatusTorneoFalse();
}
