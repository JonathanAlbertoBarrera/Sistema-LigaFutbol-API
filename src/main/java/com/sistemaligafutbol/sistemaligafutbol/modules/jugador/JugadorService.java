package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.Solicitud;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ImgurService imgurService;

    @Transactional
    public Jugador crearJugador(JugadorDTO jugadorDTO, MultipartFile imagen) {
        if (jugadorRepository.existsByNombreCompleto(jugadorDTO.getNombreCompleto())) {
            throw new ValidationException("El nombre del jugador ya está en uso.");
        }

        if (jugadorRepository.existsByEquipo_IdAndNumeroCamiseta(jugadorDTO.getIdEquipo(), jugadorDTO.getNumero_camiseta())) {
            throw new ValidationException("El número de camiseta ya está asignado en este equipo.");
        }

        // Obtener el equipo
        Equipo equipo = equipoRepository.findById(jugadorDTO.getIdEquipo())
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con ID: " + jugadorDTO.getIdEquipo()));

        // Contar jugadores activos en el equipo
        long jugadoresActivos = jugadorRepository.countByEquipoAndHabilitadoTrue(equipo);
        boolean equipoLleno = jugadoresActivos >= 20;

        // Buscar si el equipo está inscrito en un torneo y obtener el torneo
        Optional<Solicitud> solicitudOpt = solicitudRepository.findByEquipoAndResolucionTrue(equipo);
        boolean esLiguilla = solicitudOpt.map(s -> s.getTorneo().isEsliguilla()).orElse(false);

        try {
            String imagenUrl = imgurService.uploadImage(imagen);

            Jugador jugador = new Jugador();
            jugador.setNombreCompleto(jugadorDTO.getNombreCompleto());
            jugador.setFechaNacimiento(jugadorDTO.getFechaNacimiento());
            jugador.setFotoJugador(imagenUrl);
            jugador.setNumeroCamiseta(jugadorDTO.getNumero_camiseta());
            jugador.setPartidosJugados(0);
            jugador.setEquipo(equipo);

            // Definir si el jugador estará habilitado o no
            jugador.setHabilitado(!(esLiguilla || equipoLleno));

            return jugadorRepository.save(jugador);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo procesar la imagen del jugador");
        }
    }

    @Transactional
    public Jugador cambiarEstatusJugador(Long idJugador) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));

        // Buscar si el equipo está inscrito en un torneo y obtener el torneo
        Optional<Solicitud> solicitudOpt = solicitudRepository.findByEquipoAndResolucionTrue(jugador.getEquipo());
        boolean esLiguilla = solicitudOpt.map(s -> s.getTorneo().isEsliguilla()).orElse(false);

        if (esLiguilla) {
            throw new ValidationException("No se puede cambiar el estatus del jugador porque su equipo está en liguilla.");
        }

        long jugadoresActivos = jugadorRepository.countByEquipoAndHabilitadoTrue(jugador.getEquipo());
        boolean equipoLleno = jugadoresActivos >= 20;

        if(equipoLleno && !jugador.isHabilitado()){
            throw new ValidationException("El equipo ya tiene 20 jugadores activos, puedes desactivar a algún jugador para liberar un espacio");
        }

        jugador.setHabilitado(!jugador.isHabilitado());
        return jugadorRepository.save(jugador);
    }


    @Transactional
    public Jugador actualizarJugador(Long id, JugadorActualizarDTO jugadorDTO, MultipartFile imagen) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado con ID: " + id));


        if (jugadorDTO.getNumero_camiseta() != jugador.getNumeroCamiseta()
                && jugadorRepository.existsByEquipo_IdAndNumeroCamiseta(jugador.getEquipo().getId(), jugadorDTO.getNumero_camiseta())) {
            throw new ValidationException("Ese número de camiseta ya está asignado en este equipo.");
        }

        if (jugadorRepository.existsByNombreCompleto(jugadorDTO.getNombreCompleto())  && !jugador.getNombreCompleto().equals(jugadorDTO.getNombreCompleto())) {
            throw new ValidationException("El nombre del jugador ya está en uso.");
        }

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                jugador.setFotoJugador(nuevaImagenUrl);
            }
            jugador.setNombreCompleto(jugadorDTO.getNombreCompleto());
            jugador.setFechaNacimiento(jugadorDTO.getFechaNacimiento());
            jugador.setNumeroCamiseta(jugadorDTO.getNumero_camiseta());

            return jugadorRepository.save(jugador);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo actualizar la imagen del jugador");
        }
    }


    @Transactional(readOnly = true)
    public List<Jugador> obtenerTodosLosJugadores() {
        return jugadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Jugador obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Jugador> obtenerJugadoresPorEquipo(Long idEquipo) {
        if (!equipoRepository.existsById(idEquipo)) {
            throw new NotFoundException("Equipo no encontrado con ID: " + idEquipo);
        }
        return jugadorRepository.findByEquipo_Id(idEquipo);
    }




}

