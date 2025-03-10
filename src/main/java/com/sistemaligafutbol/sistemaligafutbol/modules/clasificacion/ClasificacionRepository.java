package com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface ClasificacionRepository extends JpaRepository<Clasificacion,Long> {
    // Obtener clasificación por torneo
    List<Clasificacion> findByTorneo(Torneo torneo);
    @Query("SELECT c.equipo FROM Clasificacion c WHERE c.torneo = :torneo " +
            "ORDER BY c.puntos DESC, " +
            "(c.golesAFavor - c.golesEnContra) DESC, " +
            "c.golesAFavor DESC, " +
            "c.golesEnContra ASC, " +
            "c.partidosGanados DESC, " +
            "c.partidosPerdidos ASC")
    List<Equipo> findTopEquiposByClasificacion(@Param("torneo") Torneo torneo);
}
