package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    List<Equipo> findByDueno(Dueno dueno);
}
