package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface PartidoRepository extends JpaRepository<Partido,Long> {
    // Obtener partidos por torneo
    List<Partido> findByTorneo(Torneo torneo);
    // Buscar partidos por cancha y fecha
    List<Partido> findByCanchaAndFechaPartido(Cancha cancha, LocalDate fechaPartido);

    // Buscar partidos por árbitro y fecha
    List<Partido> findByArbitroAndFechaPartido(Arbitro arbitro, LocalDate fechaPartido);
    List<Partido> findByTorneoAndJugadoFalse(Torneo torneo);

    boolean existsByArbitroAndJugadoFalse(Arbitro arbitro);

    // Verifica si hay partidos no jugados en una cancha específica
    boolean existsByCanchaAndJugadoFalse(Cancha cancha);

    List<Partido> findByEquipoLocalIdOrEquipoVisitanteId(Long idLocal, Long idVisitante);

    List<Partido> findByTorneoAndEquipoLocalIdOrEquipoVisitanteId(Torneo torneo, Long idLocal, Long idVisitante);

    List<Partido> findByTorneoAndJugadoFalseAndEquipoLocalIdOrEquipoVisitanteId(Torneo torneo, Long idLocal, Long idVisitante);

    Optional<Partido> findTopByJugadoFalseAndEquipoLocalIdOrEquipoVisitanteIdOrderByFechaPartidoAsc(Long idLocal, Long idVisitante);
    Optional<Partido> findTopByJugadoFalseOrderByFechaPartidoAsc();
    // Método personalizado para obtener los equipos clasificados por torneo
    @Query("SELECT DISTINCT p.equipoLocal FROM Partido p WHERE p.torneo = :torneo " +
            "AND p.golesLocal > p.golesVisitante UNION " +
            "SELECT DISTINCT p.equipoVisitante FROM Partido p WHERE p.torneo = :torneo " +
            "AND p.golesVisitante > p.golesLocal")
    List<Equipo> findEquiposClasificados(@Param("torneo") Torneo torneo);
    @Query("SELECT p FROM Partido p WHERE p.torneo = :torneo AND p.isFinal = true")
    Partido findFinalByTorneo(@Param("torneo") Torneo torneo);
    @Query("SELECT p FROM Partido p WHERE p.torneo = :torneo AND p.torneo.esliguilla = true")
    List<Partido> findByTorneoAndEsLiguillaTrue(@Param("torneo") Torneo torneo);
    List<Partido> findByTorneoAndJugadoTrue(Torneo torneo);
}
