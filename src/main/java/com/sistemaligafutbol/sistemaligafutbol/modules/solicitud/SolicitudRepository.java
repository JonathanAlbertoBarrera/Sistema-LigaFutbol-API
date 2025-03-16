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
    List<Solicitud> findByEquipoAndResolucionTrueAndInscripcionEstatusTrue(Equipo equipo);
    boolean existsByEquipoAndTorneo_IdAndResolucionTrueAndInscripcionEstatusTrue(Equipo equipo, Long torneoId);
    // Obtener la solicitud asociada a un equipo
    Optional<Solicitud> findByEquipo(Equipo equipo);

    // Contar solicitudes aceptadas y con pago completado en un torneo
    long countByTorneoAndResolucionTrueAndInscripcionEstatusTrue(Torneo torneo);

    // Obtener solicitudes aceptadas y con pago completado en un torneo
    List<Solicitud> findByTorneoAndResolucionTrueAndInscripcionEstatusTrue(Torneo torneo);

    Optional<Solicitud> findByEquipoAndTorneo(Equipo equipo, Torneo torneo);
    void deleteByEquipoAndTorneo(Equipo equipo, Torneo torneo);

    boolean existsByEquipoAndTorneo(Equipo equipo, Torneo torneo);

    boolean existsByEquipoAndResolucionTrueAndInscripcionEstatusTrueAndTorneoEstatusTorneoTrue(Equipo equipo);
    // Buscar todas las solicitudes pendientes de una lista de equipos
    List<Solicitud> findByEquipoInAndPendienteTrue(List<Equipo> equipos);

    //  Encuentra todas las solicitudes aceptadas pero sin pagar la inscripción
    List<Solicitud> findByEquipoInAndResolucionTrueAndInscripcionEstatusFalse(List<Equipo> equipos);

    List<Solicitud> findByEquipoAndResolucionTrueAndInscripcionEstatusTrueAndTorneoEstatusTorneoTrue(Equipo equipo);
}
