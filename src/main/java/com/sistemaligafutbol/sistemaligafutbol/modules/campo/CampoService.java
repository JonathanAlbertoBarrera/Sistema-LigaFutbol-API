package com.sistemaligafutbol.sistemaligafutbol.modules.campo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.CanchaRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampoService {
    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private PartidoRepository partidoRepository;

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

    @Transactional
    public String cambiarEstatusCampo(Long idCampo) {
        Campo campo = campoRepository.findById(idCampo)
                .orElseThrow(() -> new NotFoundException("Campo no encontrado"));

        boolean nuevoEstatus = !campo.isEstatusCampo(); // Cambia el estatus (activo/inactivo)

        if (!nuevoEstatus) { // Si se va a **desactivar**
            boolean tienePartidosPendientes = canchaRepository.findByCampo(campo).stream()
                    .anyMatch(cancha -> partidoRepository.existsByCanchaAndJugadoFalse(cancha));

            if (tienePartidosPendientes) {
                throw new ValidationException("No se puede desactivar el campo, ya que algunas de sus canchas tienen partidos pendientes.");
            }

            // Desactivar todas las canchas del campo
            List<Cancha> canchasDelCampo = canchaRepository.findByCampo(campo);
            canchasDelCampo.forEach(cancha -> cancha.setEstatusCancha(false));
            canchaRepository.saveAll(canchasDelCampo);
        }

        // Cambiar el estatus del campo
        campo.setEstatusCampo(nuevoEstatus);
        campoRepository.save(campo);

        return nuevoEstatus
                ? "Campo activado correctamente."
                : "Campo desactivado correctamente. Se desactivaron " + canchaRepository.countByCampoAndEstatusCanchaFalse(campo) + " canchas.";
    }


}
