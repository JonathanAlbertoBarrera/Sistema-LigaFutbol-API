package com.sistemaligafutbol.sistemaligafutbol.modules.campo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampoService {
    @Autowired
    private CampoRepository campoRepository;

    @Transactional
    public Campo crearCampo(CampoDTO campoDTO) {
        Campo campo = new Campo();
        campo.setNombre(campoDTO.getNombre());
        campo.setDireccion(campoDTO.getDireccion());
        campo.setLatitud(campoDTO.getLatitud());
        campo.setLongitud(campoDTO.getLongitud());
        campo.setEstatusCampo(true);
        return campoRepository.save(campo);
    }

    @Transactional
    public Campo actualizarCampo(Long id, CampoDTO campoDTO) {
        Campo campo = campoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campo no encontrado"));

        campo.setNombre(campoDTO.getNombre());
        campo.setDireccion(campoDTO.getDireccion());
        campo.setLatitud(campoDTO.getLatitud());
        campo.setLongitud(campoDTO.getLongitud());

        return campoRepository.save(campo);
    }

    @Transactional(readOnly = true)
    public List<Campo> obtenerCampos() {
        return campoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Campo> obtenerCamposActivos(){
        return campoRepository.findByEstatusCampoTrue();
    }

    @Transactional(readOnly = true)
    public Campo obtenerCampoPorId(Long id) {
        return campoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campo no encontrado"));
    }

}
