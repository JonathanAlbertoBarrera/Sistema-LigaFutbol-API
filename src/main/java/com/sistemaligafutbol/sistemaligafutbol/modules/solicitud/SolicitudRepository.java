package com.sistemaligafutbol.sistemaligafutbol.modules.solicitud;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud,Long> {
    List<Solicitud> findByPendienteTrue();
    List<Solicitud> findByTorneo(Torneo torneo);
    List<Solicitud> findByTorneoAndPendienteTrue(Torneo torneo);
    List<Solicitud> findByTorneoAndResolucionTrue(Torneo torneo);
    List<Solicitud> findByEquipo_Dueno_Id(Long idDueno);
    Optional<Solicitud> findByEquipoAndResolucionTrue(Equipo equipo);
    // Verifica si un equipo tiene una solicitud aceptada
    boolean existsByEquipo_IdAndResolucionTrue(Long equipoId);

    // Verifica si un equipo está en un torneo específico
    boolean existsByEquipo_IdAndTorneo_Id(Long equipoId, Long torneoId);
}
