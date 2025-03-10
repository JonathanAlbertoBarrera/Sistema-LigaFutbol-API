package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JugadorEstadisticaRepository extends JpaRepository<JugadorEstadistica,Long> {
    List<JugadorEstadistica> findByPartido(Partido partido);
    long countByJugadorAndPartido_FechaPartidoAfter(Jugador jugador, LocalDate fecha);
}
