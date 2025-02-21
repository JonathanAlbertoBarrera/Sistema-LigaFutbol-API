package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.CampoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanchaService {
    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private CampoRepository campoRepository;

    public Cancha crearCancha(CanchaDTO canchaDTO) {
        Campo campo = campoRepository.findById(canchaDTO.getIdCampo())
                .orElseThrow(() -> new NotFoundException("Campo no encontrado"));

        Cancha cancha = new Cancha();
        cancha.setNumeroCancha(canchaDTO.getNumeroCancha());
        cancha.setDescripcion(canchaDTO.getDescripcion());
        cancha.setEstatusCancha(true);
        cancha.setCampo(campo);

        return canchaRepository.save(cancha);
    }

    public Cancha actualizarCancha(Long id, CanchaDTO canchaDTO) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));

        cancha.setNumeroCancha(canchaDTO.getNumeroCancha());
        cancha.setDescripcion(canchaDTO.getDescripcion());


        return canchaRepository.save(cancha);
    }

    public List<Cancha> obtenerCanchas() {
        return canchaRepository.findAll();
    }

    public Cancha obtenerCanchaPorId(Long id) {
        return canchaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));
    }
}

