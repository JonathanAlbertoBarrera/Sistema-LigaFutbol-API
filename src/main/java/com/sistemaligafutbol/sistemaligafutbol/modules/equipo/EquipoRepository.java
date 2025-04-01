package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    List<Equipo> findByDueno(Dueno dueno);
    Optional<Equipo> findByNombreEquipo(String nombreEquipo);
}
