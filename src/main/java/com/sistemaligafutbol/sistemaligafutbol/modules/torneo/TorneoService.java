package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
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
        return torneoRepository.findByEstatusTorneoFalseAndIniciadoTrue();
    }

}
