package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.GoogleDriveService;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.Pago;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.PagoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.Solicitud;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class TorneoService {
    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private GoogleDriveService imagenService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Transactional
    public Torneo registrarTorneo(TorneoDTO torneoDTO, MultipartFile imagen) {
        try {
            // SUBIR LA IMAGEN
            String imagenUrl = imagenService.uploadImage(imagen);

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
            torneo.setEsliguilla(false);
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

        if (hoy.isAfter(inicioLiguilla) || hoy.isAfter(torneo.getFechaFin())) {
            throw new IllegalStateException("No se puede modificar un torneo que ya está en la etapa de liguilla o ha finalizado.");
        }

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imagenService.uploadImage(imagen);
                torneo.setLogoTorneo(nuevaImagenUrl);
            }

            torneo.setNombreTorneo(torneoDTO.getNombreTorneo());
            torneo.setDescripcion(torneoDTO.getDescripcion());
            torneo.setPremio(torneoDTO.getPremio());

            if (!torneo.isIniciado()) {
                torneo.setMaxEquipos(torneoDTO.getMaxEquipos());
                torneo.setMinEquipos(torneoDTO.getMinEquipos());
                torneo.setFechaInicio(torneoDTO.getFechaInicio());
                torneo.setVueltas(torneoDTO.getVueltas());
                torneo.setEquiposLiguilla(torneoDTO.getEquiposLiguilla());

                int jornadas = (torneoDTO.getMaxEquipos() - 1) * torneoDTO.getVueltas();
                int semanasLiguilla = (int) (Math.log(torneoDTO.getEquiposLiguilla()) / Math.log(2));
                LocalDate fechaFinCalculada = torneoDTO.getFechaInicio().plusWeeks(jornadas + semanasLiguilla);
                torneo.setFechaFin(fechaFinCalculada);

                List<Solicitud> solicitudesAceptadas = solicitudRepository.findByTorneoAndResolucionTrue(torneo);
                for (Solicitud solicitud : solicitudesAceptadas) {
                    Pago pagoInscripcion = pagoRepository.findByEquipoAndTipoPago(solicitud.getEquipo(), "Inscripción")
                            .orElseThrow(() -> new NotFoundException("Pago de inscripción no encontrado"));

                    pagoInscripcion.setFechaLimitePago(Date.from(torneo.getFechaInicio().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    pagoRepository.save(pagoInscripcion);
                }
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

        if (hoy.isAfter(inicioLiguilla) || hoy.isAfter(torneo.getFechaFin())) {
            throw new IllegalStateException("No se puede cancelar un torneo que ya está en la liguilla o ha finalizado.");
        }

        List<Solicitud> solicitudes = solicitudRepository.findByTorneo(torneo);
        for (Solicitud solicitud : solicitudes) {
            List<Pago> pagosPendientes = pagoRepository.findByEquipoAndEstatusPagoFalse(solicitud.getEquipo());
            pagoRepository.deleteAll(pagosPendientes);
            pagoRepository.flush();
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
    public Torneo obtenerTorneo(Long id){
        return torneoRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Torneo no encontrado"));
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
