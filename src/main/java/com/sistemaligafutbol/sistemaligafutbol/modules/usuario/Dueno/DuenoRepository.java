package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DuenoRepository extends JpaRepository<Dueno,Long> {
    Optional<Dueno> findByUsuario(Usuario usuario);
}
