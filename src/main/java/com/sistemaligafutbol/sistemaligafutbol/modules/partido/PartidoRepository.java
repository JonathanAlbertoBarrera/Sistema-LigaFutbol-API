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
    @Query("SELECT p FROM Partido p WHERE p.torneo = :torneo AND p.isFinal = true")
    Partido findFinalByTorneo(@Param("torneo") Torneo torneo);
    @Query("SELECT p FROM Partido p WHERE p.equipoLocal = :equipo OR p.equipoVisitante = :equipo AND p.fechaPartido > :fecha")
    List<Partido> findByEquipoAndFechaPartidoAfter(@Param("equipo") Equipo equipo, @Param("fecha") LocalDate fecha);
    @Query("SELECT p FROM Partido p WHERE p.torneo = :torneo AND p.torneo.esliguilla = true")
    List<Partido> findByTorneoAndEsLiguillaTrue(@Param("torneo") Torneo torneo);
    List<Partido> findByTorneoAndJugadoTrue(Torneo torneo);
    Optional<Partido> findByTorneoAndEquipoLocalAndEquipoVisitanteAndIdaVuelta(Torneo torneo, Equipo equipoLocal, Equipo equipoVisitante, String idaVuelta);
    List<Partido> findByArbitro(Arbitro arbitro);
    List<Partido> findByArbitroAndJugadoFalse(Arbitro arbitro);
    List<Partido> findByArbitroAndJugadoTrue(Arbitro arbitro);
    // Buscar partidos de la cancha en el rango de horas
    @Query("SELECT p FROM Partido p WHERE p.cancha = :cancha AND p.fechaPartido = :fechaPartido " +
            "AND (p.hora BETWEEN :horaInicioRango AND :horaFinRango)")
    Optional<Partido> findByCanchaAndFechaPartidoBetweenHora(@Param("cancha") Cancha cancha,
                                                             @Param("fechaPartido") LocalDate fechaPartido,
                                                             @Param("horaInicioRango") LocalTime horaInicioRango,
                                                             @Param("horaFinRango") LocalTime horaFinRango);


    // Buscar partidos del árbitro en el rango de horas
    @Query("SELECT p FROM Partido p WHERE p.arbitro = :arbitro AND p.fechaPartido = :fechaPartido " +
            "AND (p.hora BETWEEN :horaInicioRango AND :horaFinRango)")
    Optional<Partido> findByArbitroAndFechaPartidoBetweenHora(@Param("arbitro") Arbitro arbitro,
                                                              @Param("fechaPartido") LocalDate fechaPartido,
                                                              @Param("horaInicioRango") LocalTime horaInicioRango,
                                                              @Param("horaFinRango") LocalTime horaFinRango);

}
