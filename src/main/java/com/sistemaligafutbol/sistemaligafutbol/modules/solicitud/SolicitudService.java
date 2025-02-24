package com.sistemaligafutbol.sistemaligafutbol.modules.solicitud;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.DuenoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarTodasSolicitudes(){
        return solicitudRepository.findAll().stream().map(solicitud ->
                new SolicitudDTO(
                        solicitud.getIdEquipoTorneo(),
                        solicitud.getEquipo().getId(),
                        solicitud.getEquipo().getNombreEquipo(),
                        solicitud.getTorneo().getId(),
                        solicitud.getTorneo().getNombreTorneo(),
                        solicitud.getInscripcionEstatus(),
                        solicitud.getResolucion(),
                        solicitud.getPendiente()
                )
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarTodasSolicitudesPendientes() {

        return solicitudRepository.findByPendienteTrue().stream().map(solicitud ->
                new SolicitudDTO(
                        solicitud.getIdEquipoTorneo(),
                        solicitud.getEquipo().getId(),
                        solicitud.getEquipo().getNombreEquipo(),
                        solicitud.getTorneo().getId(),
                        solicitud.getTorneo().getNombreTorneo(),
                        solicitud.getInscripcionEstatus(),
                        solicitud.getResolucion(),
                        solicitud.getPendiente()
                )
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarTodasSolicitudesPorTorneo(Long torneoId) {
        Torneo torneo=torneoRepository.findById(torneoId)
                .orElseThrow(()-> new NotFoundException("Torneo no encontrado"));

        return solicitudRepository.findByTorneo(torneo).stream().map(solicitud ->
                new SolicitudDTO(
                        solicitud.getIdEquipoTorneo(),
                        solicitud.getEquipo().getId(),
                        solicitud.getEquipo().getNombreEquipo(),
                        solicitud.getTorneo().getId(),
                        solicitud.getTorneo().getNombreTorneo(),
                        solicitud.getInscripcionEstatus(),
                        solicitud.getResolucion(),
                        solicitud.getPendiente()
                )
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarSolicitudesPendientesPorTorneo(Long torneoId) {
        Torneo torneo=torneoRepository.findById(torneoId)
                .orElseThrow(()-> new NotFoundException("Torneo no encontrado"));

        return solicitudRepository.findByTorneoAndPendienteTrue(torneo).stream().map(solicitud ->
                new SolicitudDTO(
                        solicitud.getIdEquipoTorneo(),
                        solicitud.getEquipo().getId(),
                        solicitud.getEquipo().getNombreEquipo(),
                        solicitud.getTorneo().getId(),
                        solicitud.getTorneo().getNombreTorneo(),
                        solicitud.getInscripcionEstatus(),
                        solicitud.getResolucion(),
                        solicitud.getPendiente()
                )
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarSolicitudesAceptadasPorTorneo(Long torneoId) {
        Torneo torneo=torneoRepository.findById(torneoId)
                .orElseThrow(()-> new NotFoundException("Torneo no encontrado"));

        return solicitudRepository.findByTorneoAndResolucionTrue(torneo).stream().map(solicitud ->
                new SolicitudDTO(
                        solicitud.getIdEquipoTorneo(),
                        solicitud.getEquipo().getId(),
                        solicitud.getEquipo().getNombreEquipo(),
                        solicitud.getTorneo().getId(),
                        solicitud.getTorneo().getNombreTorneo(),
                        solicitud.getInscripcionEstatus(),
                        solicitud.getResolucion(),
                        solicitud.getPendiente()
                )
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarSolicitudesPorDueno(Long idUsuario) {
        Usuario usuario=usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        Dueno dueno=duenoRepository.findByUsuario(usuario)
                .orElseThrow(()->new NotFoundException("Dueño no encontrado"));
        Long idDueno=dueno.getId();
        return solicitudRepository.findByEquipo_Dueno_Id(idDueno)
                .stream().map(solicitud ->
                        new SolicitudDTO(
                                solicitud.getIdEquipoTorneo(),
                                solicitud.getEquipo().getId(),
                                solicitud.getEquipo().getNombreEquipo(),
                                solicitud.getTorneo().getId(),
                                solicitud.getTorneo().getNombreTorneo(),
                                solicitud.getInscripcionEstatus(),
                                solicitud.getResolucion(),
                                solicitud.getPendiente()
                        )
                ).collect(Collectors.toList());
    }

    @Transactional
    public SolicitudDTO crearSolicitud(Long idEquipo, Long idTorneo) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        Solicitud solicitud = new Solicitud();
        solicitud.setEquipo(equipo);
        solicitud.setTorneo(torneo);
        solicitud.setInscripcionEstatus(false);
        solicitud.setResolucion(false);
        solicitud.setPendiente(true);//por defecto al crear solicitud la hacemos pendiente

        solicitud = solicitudRepository.save(solicitud);

        return new SolicitudDTO(
                solicitud.getIdEquipoTorneo(),
                solicitud.getEquipo().getId(),
                solicitud.getEquipo().getNombreEquipo(),
                solicitud.getTorneo().getId(),
                solicitud.getTorneo().getNombreTorneo(),
                solicitud.getInscripcionEstatus(),
                solicitud.getResolucion(),
                solicitud.getPendiente()
        );
    }

    @Transactional
    public SolicitudDTO agregarEquipo(Long idEquipo, Long idTorneo) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        Solicitud solicitud = new Solicitud();
        solicitud.setEquipo(equipo);
        solicitud.setTorneo(torneo);
        solicitud.setInscripcionEstatus(false);
        solicitud.setResolucion(true);//por defecto como el admin agrega al equipo, la resolucion es true
        solicitud.setPendiente(false);//no estaria pendiente ya que no hay que aceptar la solicitud, automaticamente se hizo.

        solicitud = solicitudRepository.save(solicitud);

        return new SolicitudDTO(
                solicitud.getIdEquipoTorneo(),
                solicitud.getEquipo().getId(),
                solicitud.getEquipo().getNombreEquipo(),
                solicitud.getTorneo().getId(),
                solicitud.getTorneo().getNombreTorneo(),
                solicitud.getInscripcionEstatus(),
                solicitud.getResolucion(),
                solicitud.getPendiente()
        );
    }

    @Transactional
    public String aceptarSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        solicitud.setResolucion(true);
        solicitud.setPendiente(false);//al dar una resolucion, ya no esta pendiente
        solicitudRepository.save(solicitud);

        return "Solicitud aceptada correctamente.";
    }

    @Transactional
    public String rechazarSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        solicitud.setResolucion(false);
        solicitud.setPendiente(false);//al dar una resolucion, ya no esta pendiente
        solicitudRepository.save(solicitud);

        return "Solicitud rechazada correctamente.";
    }


}

