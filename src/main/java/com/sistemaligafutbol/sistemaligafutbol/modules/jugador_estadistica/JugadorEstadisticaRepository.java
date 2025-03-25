package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JugadorEstadisticaRepository extends JpaRepository<JugadorEstadistica,Long> {
    List<JugadorEstadistica> findByPartido(Partido partido);
    long countByJugadorAndPartido_FechaPartidoAfter(Jugador jugador, LocalDate fecha);
    // Obtener la tabla de goleo por torneo, ordenada por goles
    @Query("SELECT je FROM JugadorEstadistica je WHERE je.jugador = :jugador ORDER BY je.partido.fechaPartido DESC")
    List<JugadorEstadistica> findByJugadorOrderByPartido_FechaPartidoDesc(@Param("jugador") Jugador jugador);


    @Query("SELECT new com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica.ResponseTablaGoleo(" +
            "j.jugador.id, j.jugador.nombreCompleto, j.jugador.fotoJugador, SUM(j.goles), " +
            "COUNT(DISTINCT j.partido), j.jugador.equipo.logo) " +  // Agregamos el logo del equipo
            "FROM JugadorEstadistica j " +
            "WHERE j.partido.torneo.id = :idTorneo AND j.goles > 0 " +
            "GROUP BY j.jugador.id, j.jugador.nombreCompleto, j.jugador.fotoJugador, j.jugador.equipo.logo " +
            "ORDER BY SUM(j.goles) DESC")
    List<ResponseTablaGoleo> findByPartido_Torneo_IdOrderByGolesDesc(@Param("idTorneo") Long idTorneo);

}
