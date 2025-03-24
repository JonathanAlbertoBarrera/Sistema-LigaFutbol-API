package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    // Buscar jugadores por equipo (accediendo al objeto Equipo)
    List<Jugador> findByEquipo_Id(Long equipoId);

    // Verificar si un número de camiseta ya existe en el equipo
    boolean existsByEquipo_IdAndNumeroCamiseta(Long equipoId, int numeroCamiseta);

    // Verificar si el nombre de jugador ya existe (nombre único)
    boolean existsByNombreCompleto(String nombreCompleto);

    long countByEquipoAndHabilitadoTrue(Equipo equipo);

    List<Jugador> findByEquipo(Equipo equipo);
    List<Jugador> findByExpulsadoTrue();
    List<Jugador> findByEquipo_IdAndHabilitadoTrue(Long idEquipo);
    List<Jugador> findByEquipo_IdAndHabilitadoTrueAndExpulsadoFalse(Long idEquipo);
    // Método para obtener los jugadores de un torneo, usando la relación con `Equipo`
    // Consultar los jugadores de los equipos confirmados en un torneo
    @Query("SELECT j FROM Jugador j WHERE j.equipo IN (" +
            "SELECT s.equipo FROM Solicitud s WHERE s.torneo = :torneo " +
            "AND s.resolucion = true AND s.inscripcionEstatus = true)" )
    List<Jugador> findJugadoresPorTorneo(@Param("torneo") Torneo torneo);
}


