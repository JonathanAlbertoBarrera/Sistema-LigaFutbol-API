package com.sistemaligafutbol.sistemaligafutbol.modules.campo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampoRepository extends JpaRepository<Campo,Long> {
    List<Campo> findByEstatusCampoTrue();
}
