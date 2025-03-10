package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
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
}


