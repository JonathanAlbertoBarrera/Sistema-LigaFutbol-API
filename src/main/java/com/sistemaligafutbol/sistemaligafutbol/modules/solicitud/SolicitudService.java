package com.sistemaligafutbol.sistemaligafutbol.modules.solicitud;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.Pago;
import com.sistemaligafutbol.sistemaligafutbol.modules.pago.PagoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.DuenoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
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

    @Autowired
    private PagoRepository pagoRepository;

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
    public SolicitudDTO obtenerSolicitudPorId(Long idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

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
    public List<SolicitudDTO> listarEquiposConfirmadosPorTorneo(Long torneoId) {
        Torneo torneo=torneoRepository.findById(torneoId)
                .orElseThrow(()-> new NotFoundException("Torneo no encontrado"));

        return solicitudRepository.findByTorneoAndResolucionTrueAndInscripcionEstatusTrue(torneo).stream().map(solicitud ->
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

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarSolicitudesPorEquipo(Long idEquipo) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));

        return solicitudRepository.findByEquipo(equipo).stream().map(solicitud ->
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

        // Verificar si el equipo ya tiene una solicitud en este torneo
        if (solicitudRepository.existsByEquipoAndTorneo(equipo, torneo)) {
            throw new ValidationException("El equipo ya tiene una solicitud en este torneo.");
        }

        // Verificar si el torneo ya está lleno
        if (torneo.isEstatusLlenado()) {
            throw new ValidationException("El torneo ya ha alcanzado el número máximo de equipos permitidos.");
        }

        // Crear la solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setEquipo(equipo);
        solicitud.setTorneo(torneo);
        solicitud.setInscripcionEstatus(false);
        solicitud.setResolucion(false);
        solicitud.setPendiente(true);

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

        Torneo torneo = solicitud.getTorneo();
        Equipo equipo = solicitud.getEquipo();

        // Verificar si el torneo ya está lleno
        long equiposAceptados = solicitudRepository.countByTorneoAndResolucionTrueAndInscripcionEstatusTrue(torneo);
        if (equiposAceptados >= torneo.getMaxEquipos()) {
            throw new ValidationException("El torneo ya ha alcanzado el número máximo de equipos permitidos.");
        }

        // Aceptar la solicitud
        solicitud.setResolucion(true);
        solicitud.setPendiente(false);
        solicitudRepository.save(solicitud);

        // Generar pago de inscripción
        Pago pagoInscripcion = new Pago();
        pagoInscripcion.setTipoPago("Inscripción");
        pagoInscripcion.setMonto(1000);
        pagoInscripcion.setFechaPago(null);
        pagoInscripcion.setFechaLimitePago(Date.from(torneo.getFechaInicio().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        pagoInscripcion.setEstatusPago(false);
        pagoInscripcion.setEquipo(equipo);
        pagoInscripcion.setDescripcion("Inscripción "+torneo.getNombreTorneo());
        pagoRepository.save(pagoInscripcion);

        return "Solicitud aceptada correctamente. El pago de inscripción ha sido generado.";
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

