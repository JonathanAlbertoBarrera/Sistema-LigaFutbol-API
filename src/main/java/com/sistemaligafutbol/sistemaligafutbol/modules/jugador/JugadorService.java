package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
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
    private ImgurService imgurService;

    @Transactional
    public Jugador crearJugador(JugadorDTO jugadorDTO, MultipartFile imagen) {
        try {
            String imagenUrl = imgurService.uploadImage(imagen);
            Jugador jugador = new Jugador();
            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());
            jugador.setImagenUrl(imagenUrl);
            return jugadorRepository.save(jugador);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo procesar la imagen del jugador");
        }
    }

    @Transactional
    public Jugador actualizarJugador(Long id, JugadorDTO jugadorDTO, MultipartFile imagen) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Jugador no encontrado con ID: " + id));

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                jugador.setImagenUrl(nuevaImagenUrl);
            }
            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());

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
