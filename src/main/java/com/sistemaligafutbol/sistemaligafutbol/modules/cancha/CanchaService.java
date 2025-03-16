package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.CampoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CanchaService {
    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Transactional
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

    @Transactional
    public Cancha actualizarCancha(Long id, CanchaDTO canchaDTO) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));

        cancha.setNumeroCancha(canchaDTO.getNumeroCancha());
        cancha.setDescripcion(canchaDTO.getDescripcion());


        return canchaRepository.save(cancha);
    }

    @Transactional
    public String cambiarEstatusCancha(Long idCancha) {
        Cancha cancha = canchaRepository.findById(idCancha)
                .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));

        // Verificar si la cancha tiene partidos asignados aún no jugados
        if (cancha.isEstatusCancha() && partidoRepository.existsByCanchaAndJugadoFalse(cancha)) {
            throw new ValidationException("No se puede desactivar la cancha porque tiene partidos pendientes por jugar.");
        }

        // Cambiar el estatus (si estaba activa, se desactiva; si estaba inactiva, se activa)
        cancha.setEstatusCancha(!cancha.isEstatusCancha());
        canchaRepository.save(cancha);

        return "Estatus de la cancha actualizado correctamente.";
    }

    @Transactional(readOnly = true)
    public List<Cancha> obtenerCanchas() {
        return canchaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cancha obtenerCanchaPorId(Long id) {
        return canchaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));
    }
}

