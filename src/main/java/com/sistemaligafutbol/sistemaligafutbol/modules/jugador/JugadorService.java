package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private ImgurService imgurService;

    @Transactional
    public Jugador crearJugador(JugadorDTO jugadorDTO, MultipartFile imagen) {
        try {
            String imagenUrl = (imagen != null && !imagen.isEmpty()) ? imgurService.uploadImage(imagen) : null;

            Equipo equipo = equipoRepository.findById(jugadorDTO.getIdEquipo())
                    .orElseThrow(() -> new NotFoundException("Equipo no encontrado con ID: " + jugadorDTO.getIdEquipo()));

            Jugador jugador = new Jugador();
            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());
            jugador.setFechaNacimiento(jugadorDTO.getFechaNacimiento());
            jugador.setImagenUrl(imagenUrl);
            jugador.setHabilitado(true);
            jugador.setPartidosJugados(0);
            jugador.setEquipo(equipo);

            return jugadorRepository.save(jugador);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo procesar la imagen del jugador");
        }
    }

    @Transactional
    public Jugador actualizarJugador(Long id, JugadorActualizarDTO jugadorDTO, MultipartFile imagen) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado con ID: " + id));

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                jugador.setImagenUrl(nuevaImagenUrl);
            }
            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());
            jugador.setFechaNacimiento(jugadorDTO.getFechaNacimiento());

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

    public void eliminarJugador(Long id) {
        if (!jugadorRepository.existsById(id)) {
            throw new NotFoundException("Jugador no encontrado");
        }
        jugadorRepository.deleteById(id);
    }
}
