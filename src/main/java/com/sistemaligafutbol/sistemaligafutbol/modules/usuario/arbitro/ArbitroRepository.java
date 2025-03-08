package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArbitroRepository extends JpaRepository<Arbitro,Long> {
    Optional<Arbitro> findByUsuario(Usuario usuario);
    boolean existsByUsuario(Usuario usuario);
}
