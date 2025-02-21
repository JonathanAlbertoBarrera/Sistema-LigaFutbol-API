package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class TorneoService {
    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private ImgurService imgurService;

    @Transactional
    public Torneo registrarTorneo(TorneoDTO torneoDTO, MultipartFile imagen) {
        try {
            // SUBIR LA IMAGEN
            String imagenUrl = imgurService.uploadImage(imagen);

            // Crear torneo
            Torneo torneo = new Torneo();
            torneo.setNombreTorneo(torneoDTO.getNombreTorneo());
            torneo.setDescripcion(torneoDTO.getDescripcion());
            torneo.setPremio(torneoDTO.getPremio());
            torneo.setFechaInicio(torneoDTO.getFechaInicio());
            torneo.setVueltas(torneoDTO.getVueltas());

            // Calcular la fecha de fin
            int jornadas = (torneoDTO.getMaxEquipos() - 1) * torneoDTO.getVueltas();
            int semanasLiguilla = (int) (Math.log(torneoDTO.getEquiposLiguilla()) / Math.log(2)); // Se calcula como log2(equiposLiguilla)
            int totalSemanas = jornadas + semanasLiguilla;
            LocalDate fechaFinCalculada = torneoDTO.getFechaInicio().plusWeeks(totalSemanas);
            torneo.setFechaFin(fechaFinCalculada);

            torneo.setMaxEquipos(torneoDTO.getMaxEquipos());
            torneo.setMinEquipos(torneoDTO.getMinEquipos());
            torneo.setEquiposLiguilla(torneoDTO.getEquiposLiguilla());
            torneo.setLogoTorneo(imagenUrl);
            torneo.setEstatusLlenado(false);
            torneo.setIniciado(false);
            torneo.setEstatusTorneo(true);

            return torneoRepository.save(torneo);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo procesar la imagen del torneo");
        }
    }

    @Transactional
    public Torneo actualizarTorneo(Long id, TorneoDTO torneoDTO, MultipartFile imagen) {
        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado con ID: " + id));

        LocalDate hoy = LocalDate.now();
        LocalDate inicioLiguilla = torneo.getFechaFin().minusWeeks((int) (Math.log(torneo.getEquiposLiguilla()) / Math.log(2)));

        // Si ya está en la etapa de liguilla o terminó, no se permite la modificación
        if (hoy.isAfter(inicioLiguilla) || hoy.isAfter(torneo.getFechaFin())) {
            throw new IllegalStateException("No se puede modificar un torneo que ya está en la etapa de liguilla o ha finalizado.");
        }

        try {
            // Si se proporciona una nueva imagen, actualizar la URL
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                torneo.setLogoTorneo(nuevaImagenUrl);
            }

            // Actualizar datos generales
            torneo.setNombreTorneo(torneoDTO.getNombreTorneo());
            torneo.setDescripcion(torneoDTO.getDescripcion());
            torneo.setPremio(torneoDTO.getPremio());

            // Si el torneo aún no ha iniciado, permitir cambios en maxEquipos, minEquipos, fechaInicio, vueltas y equiposLiguilla
            if (!torneo.isIniciado()) {
                torneo.setMaxEquipos(torneoDTO.getMaxEquipos());
                torneo.setMinEquipos(torneoDTO.getMinEquipos());
                torneo.setFechaInicio(torneoDTO.getFechaInicio());
                torneo.setVueltas(torneoDTO.getVueltas());
                torneo.setEquiposLiguilla(torneoDTO.getEquiposLiguilla());

                // Recalcular la fecha de fin porque se permitieron cambios
                int jornadas = (torneoDTO.getMaxEquipos() - 1) * torneoDTO.getVueltas();
                int semanasLiguilla = (int) (Math.log(torneoDTO.getEquiposLiguilla()) / Math.log(2));
                LocalDate fechaFinCalculada = torneoDTO.getFechaInicio().plusWeeks(jornadas + semanasLiguilla);
                torneo.setFechaFin(fechaFinCalculada);
            }

            return torneoRepository.save(torneo);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo actualizar la imagen del torneo");
        }
    }

    @Transactional
    public Torneo cancelarTorneo(Long id, String motivoFinalizacion) {
        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado con ID: " + id));

        LocalDate hoy = LocalDate.now();
        LocalDate inicioLiguilla = torneo.getFechaFin().minusWeeks((int) (Math.log(torneo.getEquiposLiguilla()) / Math.log(2)));

        // Validar si el torneo ya está en liguilla o finalizado
        if (hoy.isAfter(inicioLiguilla) || hoy.isAfter(torneo.getFechaFin())) {
            throw new IllegalStateException("No se puede cancelar un torneo que ya está en la liguilla o ha finalizado.");
        }

        torneo.setEstatusTorneo(false);
        torneo.setMotivoFinalizacion(motivoFinalizacion);

        return torneoRepository.save(torneo);
    }


    @Transactional(readOnly = true)
    public List<Torneo> obtenerTodosLosTorneos(){
        return torneoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosEnEspera(){
        return torneoRepository.findByEstatusTorneoTrueAndIniciadoFalse();
    }

    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosIniciados(){
        return torneoRepository.findByEstatusTorneoTrueAndIniciadoTrue();
    }

    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosFinalizados(){
        return torneoRepository.findByEstatusTorneoFalse();
    }

}
