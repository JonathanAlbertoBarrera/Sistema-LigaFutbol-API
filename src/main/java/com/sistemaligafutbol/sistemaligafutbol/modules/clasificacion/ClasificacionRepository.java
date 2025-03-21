package com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ClasificacionRepository extends JpaRepository<Clasificacion, Long> {
    List<Clasificacion> findByTorneo(Torneo torneo);

    // Obtener la clasificación de un torneo ordenada por puntos
    List<Clasificacion> findByTorneoIdOrderByPuntosDesc(Long idTorneo);
    Optional<Clasificacion> findByEquipo(Equipo equipo);
    @Query("SELECT c FROM Clasificacion c WHERE c.torneo = :torneo " +
            "ORDER BY c.puntos DESC, " +
            "(c.golesAFavor - c.golesEnContra) DESC, " +
            "c.golesAFavor DESC, " +
            "c.golesEnContra ASC, " +
            "c.partidosGanados DESC, " +
            "c.partidosPerdidos ASC")
    List<Clasificacion> findClasificacionesByTorneo(@Param("torneo") Torneo torneo);

}
