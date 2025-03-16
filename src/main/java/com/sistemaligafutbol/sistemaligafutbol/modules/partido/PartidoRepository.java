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

    // Buscar partidos por cancha, fecha y hora
    List<Partido> findByCanchaAndFechaPartidoAndHora(Cancha cancha, LocalDate fechaPartido, LocalTime hora);

    long countByTorneoAndJugadoFalse(Torneo torneo);

    @Query("SELECT p FROM Partido p WHERE p.torneo = :torneo AND (p.equipoLocal IN :equipos OR p.equipoVisitante IN :equipos)")
    List<Partido> findByTorneoAndEquiposIn(@Param("torneo") Torneo torneo, @Param("equipos") List<Equipo> equipos);

    List<Partido> findByTorneoAndJugadoFalse(Torneo torneo);
    @Query("SELECT p FROM Partido p WHERE p.torneo = :torneo AND p.jugado = true " +
            "AND p.fechaPartido = (SELECT MAX(p2.fechaPartido) FROM Partido p2 WHERE p2.torneo = :torneo AND p2.jugado = true)")
    List<Partido> findUltimosPartidosFase(@Param("torneo") Torneo torneo);

    // Obtener partidos sin jugar de una fecha específica
    List<Partido> findByTorneoAndFechaPartidoAndJugadoFalse(Torneo torneo, LocalDate fecha);

    // Contar partidos de un torneo (para verificar si es la final)
    long countByTorneo(Torneo torneo);

    List<Partido> findByTorneoAndJugadoTrue(Torneo torneo);

    // Buscar partido de ida dado un equipo local, visitante y fecha
    Optional<Partido> findByEquipoLocalAndEquipoVisitanteAndFechaPartido(
            Equipo equipoLocal, Equipo equipoVisitante, LocalDate fechaPartido);

    boolean existsByArbitroAndJugadoFalse(Arbitro arbitro);

    // Verifica si hay partidos no jugados en una cancha específica
    boolean existsByCanchaAndJugadoFalse(Cancha cancha);
}
