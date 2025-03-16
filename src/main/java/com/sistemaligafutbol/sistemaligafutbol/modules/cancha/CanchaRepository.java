package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CanchaRepository extends JpaRepository<Cancha,Long> {
    // Buscar canchas disponibles en un campo
    List<Cancha> findByCampoAndEstatusCanchaTrue(Campo campo);
    List<Cancha> findByCampo(Campo campo);
    Optional<Cancha> findByCampoAndNumeroCancha(Campo campo, int numeroCancha);
    boolean existsByCampoAndNumeroCancha(Campo campo, int numeroCancha);
    // Contar cuántas canchas están desactivadas en un campo
    long countByCampoAndEstatusCanchaFalse(Campo campo);
}

