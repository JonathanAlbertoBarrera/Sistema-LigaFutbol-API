package com.sistemaligafutbol.sistemaligafutbol.modules.convocatoria;

import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria,Long> {
    Optional<Convocatoria> findByPublicadaTrue();
    Optional<Convocatoria> findByTorneo(Torneo torneo);
}
